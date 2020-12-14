/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.deskauto.impl.win;

import net.thevpc.common.deskauto.impl.shared.DefaultDEWindowRobot;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import java.awt.Rectangle;
import net.thevpc.common.deskauto.DEWindow;
import net.thevpc.common.deskauto.DEWindowId;
import net.thevpc.common.deskauto.DesktopEnvironment;
import net.thevpc.common.deskauto.ProcessId;
import net.thevpc.common.deskauto.ContextualRobot;
import net.thevpc.common.deskauto.DEWindowRegion;

/**
 *
 * @author thevpc
 */
public class MSWinDEWindow implements DEWindow {

    private MSWinDEWindowId id;
    private WinDef.HWND handle;
    private DesktopEnvironment wm;
    private DefaultDEWindowRobot robot;

    public MSWinDEWindow(WinDef.HWND hWnd, DesktopEnvironment wm) {
        this.handle = hWnd;
        this.wm = wm;
        this.id = new MSWinDEWindowId(handle);
    }

    @Override
    public DEWindowId getId() {
        return id;
    }

    @Override
    public ProcessId getProcessId() {
        IntByReference idProcess = new IntByReference();
        User32.INSTANCE.GetWindowThreadProcessId(handle, idProcess);
        return new MSProcessId(idProcess.getValue());
    }

    @Override
    public String getModuleName() {
        IntByReference idProcess = new IntByReference();
        User32.INSTANCE.GetWindowThreadProcessId(handle, idProcess);
        int PROCESS_QUERY_INFORMATION = 1040;// (0x0400);
        WinNT.HANDLE h = Kernel32.INSTANCE.OpenProcess(PROCESS_QUERY_INFORMATION, false, idProcess.getValue());
        byte[] exePathname = new byte[1024];
        int c2 = MSWinLib.EXT_psapi.INSTANCE.GetModuleFileNameExA(h.getPointer(), null, exePathname, exePathname.length);
        String s2 = new String(exePathname, 0, c2);
        return s2;

//            char[] name = new char[1024];
//        int c = User32.INSTANCE.GetWindowModuleFileName(handle, name, name.length);
//        String s1 = new String(name, 0, c);
    }

    @Override
    public ContextualRobot getRobot() {
        if (robot == null) {
            robot = new DefaultDEWindowRobot(this);
        }
        return robot;
    }

    @Override
    public DesktopEnvironment getWindowManager() {
        return wm;
    }

    public WinDef.HWND getHandle() {
        return handle;
    }

    @Override
    public String getTitle() {
        char[] buffer = new char[User32.INSTANCE.GetWindowTextLength(handle) * 2 + 1];
        User32.INSTANCE.GetWindowText(handle, buffer, buffer.length);
        return Native.toString(buffer);
    }

    @Override
    public DEWindowRegion getClientRegion() {
        final WinDef.RECT wr = new WinDef.RECT();
        User32.INSTANCE.GetWindowRect(handle, wr);

        final WinDef.RECT r = new WinDef.RECT();
        MSWinLib.EXT_User32.INSTANCE.GetClientRect(handle, r);

        WinDef.POINT corner = new WinDef.POINT(0, 0);
        MSWinLib.EXT_User32.INSTANCE.ClientToScreen(handle, corner);
        Rectangle absolute = new Rectangle(r.left + corner.x, r.top + corner.y, r.right - r.left, r.bottom - r.top);
        Rectangle relative = new Rectangle(r.left + corner.x - wr.left, r.top + corner.y - wr.top, r.right - r.left, r.bottom - r.top);

        return new MSWinDEWindowRegion(this, relative, absolute);
    }

    @Override
    public DEWindowRegion getTitleRegion() {
        Rectangle r1 = getBounds();
        Rectangle r2 = getClientRegion().getRelativeBounds();
        Rectangle rel = new Rectangle(0, 0, r1.width, r2.y);
        Rectangle abs = new Rectangle(r1.x, r1.y, r1.width, r2.y);
        return new MSWinDEWindowRegion(this, rel, abs);
    }

    @Override
    public Rectangle getBounds() {
        final WinDef.RECT r = new WinDef.RECT();
        User32.INSTANCE.GetWindowRect(handle, r);
        return new Rectangle(r.left, r.top, r.right - r.left, r.bottom - r.top);
    }

    @Override
    public boolean moveToFront() {
        int SW_SHOWNORMAL = 1;
        int SW_SHOW = 5;
//        User32.INSTANCE.ShowWindow(handle, SW_SHOWNORMAL);
        User32.INSTANCE.ShowWindow(handle, SW_SHOW);
        User32.INSTANCE.SetForegroundWindow(handle);
        return true;
    }

    @Override
    public boolean isVisible() {
        return User32.INSTANCE.IsWindowVisible(handle);
    }

    public void setMinimized(boolean b) {
        //should call closeWindow?
        if (b && b != isMinimized()) {
            MSWinLib.WINDOWPLACEMENT p = new MSWinLib.WINDOWPLACEMENT();
            p.length = p.size();
            MSWinLib.EXT_User32.INSTANCE.GetWindowPlacement(handle, p);
//            int SW_MAXIMIZE = 3;
            int SW_MINIMIZE = 3;
            p.showCmd=SW_MINIMIZE;
            MSWinLib.EXT_User32.INSTANCE.SetWindowPlacement(handle, p);
        }
    }
    
    public void setMaximized(boolean b) {
        if (b && b != isMinimized()) {
            MSWinLib.WINDOWPLACEMENT p = new MSWinLib.WINDOWPLACEMENT();
            p.length = p.size();
            MSWinLib.EXT_User32.INSTANCE.GetWindowPlacement(handle, p);
//            int SW_MAXIMIZE = 3;
            int SW_MAXIMIZE = 3;
            p.showCmd=SW_MAXIMIZE;
            MSWinLib.EXT_User32.INSTANCE.SetWindowPlacement(handle, p);
        }
    }

    public void close() {
        User32.INSTANCE.DestroyWindow(handle);
    }
    
    @Override
    public boolean isMinimized() {
        MSWinLib.WINDOWPLACEMENT p = new MSWinLib.WINDOWPLACEMENT();
        p.length = p.size();
        MSWinLib.EXT_User32.INSTANCE.GetWindowPlacement(handle, p);
        int SW_MINIMIZE = 3;
        return ((p.showCmd & SW_MINIMIZE) != 0);
    }

    @Override
    public boolean isMaximized() {
        MSWinLib.WINDOWPLACEMENT p = new MSWinLib.WINDOWPLACEMENT();
        p.length = p.size();
        MSWinLib.EXT_User32.INSTANCE.GetWindowPlacement(handle, p);
        int SW_MAXIMIZE = 3;
        return ((p.showCmd & SW_MAXIMIZE) != 0);
    }

    @Override
    public boolean isTool() {
        int GWL_EXSTYLE = -20;
        long WS_EX_TOOLWINDOW = 0x00000080L;
        // Tool windows should not be displayed either, these do not appear in the
        // task bar.
        if ((User32.INSTANCE.GetWindowLong(handle, GWL_EXSTYLE) & WS_EX_TOOLWINDOW) != 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isChild() {
        int GA_ROOTOWNER = 3;
        long hwndi = Pointer.nativeValue(handle.getPointer());
        long hwndTry, hwndWalk = 0;
        hwndTry = MSWinLib.EXT_User32.INSTANCE.GetAncestor(hwndi, GA_ROOTOWNER);
        while (hwndTry != hwndWalk) {
            hwndWalk = hwndTry;
            hwndTry = MSWinLib.EXT_User32.INSTANCE.GetLastActivePopup(hwndWalk);
            if (User32.INSTANCE.IsWindowVisible(new WinDef.HWND(new Pointer(hwndTry)))) {
                break;
            }
        }
        if (hwndWalk != hwndi) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isSpecial() {
        String tt = getTitle();
        long hwndi = Pointer.nativeValue(handle.getPointer());
        // the following removes some task tray programs and "Program Manager"
        MSWinLib.TITLEBARINFO ti = new MSWinLib.TITLEBARINFO();
        ti.cbSize = ti.size();
        MSWinLib.EXT_User32.INSTANCE.GetTitleBarInfo(hwndi, ti);
        long STATE_SYSTEM_INVISIBLE = 0x00008000;
        if ((ti.rgstate[0] & STATE_SYSTEM_INVISIBLE) != 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isRegular() {
        return isVisible() && !isSpecial();
    }

    @Override
    public String toString() {
        return "Window{" +"@"+getId()+"="+getTitle()+ '}';
    }
    
}
