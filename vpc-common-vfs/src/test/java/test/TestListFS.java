/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import net.thevpc.common.vfs.VFile;

/**
 *
 * @author thevpc
 */
public class TestListFS {

//    public static void main(String[] args) {
//        VirtualFileSystem n = VFS.createNativeFS();
//        MountableFS m = VFS.createMountableFS();
//        ListFS l = VFS.createListFS();
//        try {
////            m.mount("/", l);
//            m.mount("/r/y", n.get("/home/vpc/.dia/"));
//            m.get("/");
//            m.mount("/r", n.get("/home/vpc/.beyondcompare/"));
//            m.get("/");
//            m.mount("/r/ff", n.get("/home/vpc/Untitled.scad"));
//            m.get("/");
//            println(m.get("/"), 4);
//        } catch (IOException ex) {
//            Logger.getLogger(TestListFS.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    private static String indent(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private static void println(VFile f, int depth) {
        println(f, depth,0);
    }
    private static void println(VFile f, int depth, int indent) {
        System.out.print(indent(indent));
        System.out.print(f.getName());
        if (f.isDirectory()) {
            System.out.print("/\n");
            if (depth > 0) {
                for (VFile ff : f.listFiles()) {
                    println(ff, depth - 1, indent + 2);
                }
            }
        } else {
            System.out.print("\n");
        }
    }
}
