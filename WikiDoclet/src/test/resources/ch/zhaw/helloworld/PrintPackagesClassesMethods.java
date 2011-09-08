package ch.zhaw.helloworld;

import com.sun.javadoc.*;

public class PrintPackagesClassesMethods {
    public static boolean start(RootDoc root) {
    	System.out.println("HI ALL ROLF KOCH!!");
    	ClassDoc[] classes = root.specifiedClasses();
    	PackageDoc[] packages = root.specifiedPackages();
    	MethodDoc[] methods;
    	
    	for (int i = 0; i < classes.length; ++i) {
            System.out.println("Classes: " + classes[i]);
         
        }
    	
        //ClassDoc[] classes = root.classes();
    	 System.out.println(root.toString());
    	for (int i = 0; i < packages.length; ++i) {
            System.out.println("Packages: " + packages[i]);
            classes = packages[i].allClasses();
            for (int j = 0; j < classes.length; ++j) {
                System.out.println("  Classes: " + classes[j]);
                try {
                	methods = classes[j].methods();
                	for (int k = 0; k < methods.length; ++k) {
                    
                		System.out.println("    Methods: " + methods[k]);
                    
                    
                	}
                }
                catch (Exception e) {
                	System.out.println("Error: " + e.getMessage() + e.getClass().toString());
                	e.printStackTrace();
                }
            }
         
        }
        
        System.out.println("BYE ALL!!");
        return true;
    }
    public PrintPackagesClassesMethods() {
    	
    }
}
