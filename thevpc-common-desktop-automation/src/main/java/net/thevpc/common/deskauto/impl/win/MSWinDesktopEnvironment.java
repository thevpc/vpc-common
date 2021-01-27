/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.deskauto.impl.win;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.thevpc.common.deskauto.DEWindow;
import net.thevpc.common.deskauto.DEWindowFilter;
import net.thevpc.common.deskauto.DesktopEnvironment;

/**
 *
 * @author thevpc
 */
public class MSWinDesktopEnvironment extends DesktopEnvironment {

    @Override
    public DEWindow findWindow(DEWindowFilter filter) {
        List<DEWindow> all = findWindows(filter);
        if (all.isEmpty()) {
            return null;
        }
        return all.get(0);
    }

    @Override
    public DEWindow findSingleWindow(DEWindowFilter filter) {
        List<DEWindow> all = findWindows(filter);
        if (all.isEmpty()) {
            throw new IllegalArgumentException("Window not found matching " + filter);
        }
        if (all.size() > 1) {
            throw new IllegalArgumentException("Too many (" + all.size() + ") windows matching " + filter);
        }
        return all.get(0);
    }

    @Override
    public List<DEWindow> findWindows(final DEWindowFilter filter) {
        return findWindowsExt(new DEWindowFilter() {

            @Override
            public boolean accept(DEWindow w) {
                if (w.isRegular()) {
                    return filter == null || filter.accept(w);
                }
                return false;
            }
        });
    }

    @Override
    public List<DEWindow> findWindowsExt(final DEWindowFilter filter) {
        final List<DEWindow> windowsList = new ArrayList<DEWindow>();
        final List<Long> order = new ArrayList<Long>();
        long top = MSWinLib.EXT_User32.INSTANCE.GetTopWindow(0);
        while (top != 0) {
            order.add(top);
            WinDef.HWND h = User32.INSTANCE.GetWindow(new WinDef.HWND(new Pointer(top)), new WinDef.DWORD(User32.GW_HWNDNEXT));
            top = Pointer.nativeValue(h == null ? null : h.getPointer());
        }
        User32.INSTANCE.EnumWindows(new WinUser.WNDENUMPROC() {

            @Override
            public boolean callback(WinDef.HWND hWnd, Pointer data) {
                MSWinDEWindow w = new MSWinDEWindow(hWnd, MSWinDesktopEnvironment.this);
                if (filter == null || filter.accept(w)) {
                    windowsList.add(w);
                }
                return true;
            }
        }, Pointer.NULL);
//        User32.INSTANCE.EnumWindows(new WndEnumProc() {
//            public boolean callback(int hWnd, int lParam) {
//                if (User32.instance.IsWindowVisible(hWnd)) {
//                    RECT r = new RECT();
//                    User32.instance.GetWindowRect(hWnd, r);
//                    if (r.left > -32000) {     // minimized
//                        byte[] buffer = new byte[1024];
//                        User32.instance.GetWindowTextA(hWnd, buffer, buffer.length);
//                        String title = Native.toString(buffer);
//                        inflList.add(new WindowInfo(hWnd, r, title));
//                    }
//                }
//                return true;
//            }
//        }, 0);
        Collections.sort(windowsList, new Comparator<DEWindow>() {
            public int compare(DEWindow o1, DEWindow o2) {
                MSWinDEWindow mo1 = (MSWinDEWindow) o1;
                MSWinDEWindow mo2 = (MSWinDEWindow) o2;
                return order.indexOf(mo1.getHandle()) - order.indexOf(mo2.getHandle());
            }
        });
        if (filter != null) {
            for (Iterator<DEWindow> i = windowsList.iterator(); i.hasNext();) {
                DEWindow w = i.next();
                if (!filter.accept(w)) {
                    i.remove();
                }
            }
        }
        return windowsList;
    }

    public static class MSWinFilter {

        private boolean acceptInvisible;
        private boolean acceptTools;

        public MSWinFilter() {
        }

        public boolean isAcceptInvisible() {
            return acceptInvisible;
        }

        public void setAcceptInvisible(boolean acceptInvisible) {
            this.acceptInvisible = acceptInvisible;
        }

        public boolean isAcceptTools() {
            return acceptTools;
        }

        public MSWinFilter setAcceptTools(boolean acceptTools) {
            this.acceptTools = acceptTools;
            return this;
        }

    }

    // http://blogs.msdn.com/b/oldnewthing/archive/2007/10/08/5351207.aspx
    //http://stackoverflow.com/questions/7277366/why-does-enumwindows-return-more-windows-than-i-expected
    boolean IsAltTabWindow(WinDef.HWND hwnd, MSWinFilter filter) {
        MSWinLib.TITLEBARINFO ti = new MSWinLib.TITLEBARINFO();
        long hwndTry, hwndWalk = 0;

        if (!filter.isAcceptInvisible()) {
            if (!User32.INSTANCE.IsWindowVisible(hwnd)) {
                return false;
            }
        }
        int GA_ROOTOWNER = 3;
        long hwndi = Pointer.nativeValue(hwnd.getPointer());
        hwndTry = MSWinLib.EXT_User32.INSTANCE.GetAncestor(hwndi, GA_ROOTOWNER);
        while (hwndTry != hwndWalk) {
            hwndWalk = hwndTry;
            hwndTry = MSWinLib.EXT_User32.INSTANCE.GetLastActivePopup(hwndWalk);
            if (User32.INSTANCE.IsWindowVisible(new WinDef.HWND(new Pointer(hwndTry)))) {
                break;
            }
        }
        if (hwndWalk != hwndi) {
            return false;
        }
        // the following removes some task tray programs and "Program Manager"
        ti.cbSize = ti.size();
        MSWinLib.EXT_User32.INSTANCE.GetTitleBarInfo(hwndi, ti);
        long STATE_SYSTEM_INVISIBLE = 0x00008000;
        if ((ti.rgstate[0] & STATE_SYSTEM_INVISIBLE) != 0) {
            return false;
        }
        int GWL_EXSTYLE = -20;
        long WS_EX_TOOLWINDOW = 0x00000080L;
        if (filter.isAcceptTools()) {
            // Tool windows should not be displayed either, these do not appear in the
            // task bar.
            if ((User32.INSTANCE.GetWindowLong(hwnd, GWL_EXSTYLE) & WS_EX_TOOLWINDOW) != 0) {
                return false;
            }
        }

        return true;
    }

}
