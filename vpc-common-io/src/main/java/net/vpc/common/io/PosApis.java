package net.vpc.common.io;

public class PosApis {
    private static PosApi instance;
    static {
        String s=System.getProperty("os.name").toLowerCase();
        if(s.contains("linux")){
            instance=new LinuxPosApi();
        }else{
            instance=new FallbackPosApi();
        }
    }

    public static PosApi get(){
        return instance;
    }
}
