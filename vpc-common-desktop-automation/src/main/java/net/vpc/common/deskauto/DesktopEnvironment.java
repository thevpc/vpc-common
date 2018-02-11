/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.deskauto;

import java.util.List;
import net.vpc.common.deskauto.impl.shared.input.DesktopContextualRobot;
import net.vpc.common.deskauto.impl.win.MSWinDesktopEnvironment;

/**
 *
 * @author vpc
 */
public abstract class DesktopEnvironment {

    private static DesktopEnvironment instance;
    private DesktopContextualRobot robot = new DesktopContextualRobot();

    public static boolean isSupported() {
        if (instance != null) {
            return true;
        }
        String dwm = System.getProperty("net.vpc.scholar.s3.DesktopWindowManager");
        if (dwm != null) {
            return true;
        }
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            return true;
        }
        return false;
    }

    private static DesktopEnvironment createInstance() {
        String dwm = System.getProperty("net.vpc.scholar.s3.DesktopWindowManager");
        if (dwm != null) {
            try {
                return (DesktopEnvironment) Class.forName(dwm).newInstance();
            } catch (Exception e) {
                throw new UnsupportedOperationException("Failed to create DesktopWindowManager  as " + dwm, e);
            }
        }
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            return new MSWinDesktopEnvironment();
        }
        throw new UnsupportedOperationException("No DesktopWindowManager Found for " + osName);
    }

    public static DesktopEnvironment getInstance() {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    public abstract DEWindow findWindow(DEWindowFilter filter);

    public abstract DEWindow findSingleWindow(DEWindowFilter filter);

    /**
     * find visible and non special windows matching filter
     *
     * @param filter
     * @return
     */
    public abstract java.util.List<DEWindow> findWindows(DEWindowFilter filter);

    /**
     * find all windows matching filter
     *
     * @param filter
     * @return
     */
    public abstract List<DEWindow> findWindowsExt(final DEWindowFilter filter);

    public ContextualRobot getRobot() {
        return robot;
    }

}
