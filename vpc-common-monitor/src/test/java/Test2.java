import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitors;

public class Test2 {
    public static void main(String[] args) {
        ProgressMonitor monitor = ProgressMonitors.logger();
        ProgressMonitor[] mon = monitor.split(0.8, 0.2);
        mon[0].setProgress(1);
        System.out.println(monitor.getProgress());
        System.out.println(mon[0].getProgress());
        System.out.println(mon[1].getProgress());

    }
}
