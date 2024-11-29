import java.io.File;
import java.util.*;

public class PluginOperations{
    public static void rm(List<File> allFiles){
        allFiles.forEach(file -> {
            file.delete();
        });
    }
    public static void rm(HashSet<String> exceptions, List<File> allFiles){
        boolean skipSignal = false;

        for(File file : allFiles){
            for(String exceptionalFile : exceptions){
                if(file.getName().equals(exceptionalFile)){
                    exceptions.remove(exceptionalFile);
                    skipSignal = true;
                    break;
                }
            }
            
            if(skipSignal){
                skipSignal = false;
                continue;
            }

            file.delete();
        }
        System.out.println("FILES REMOVED SUCCESSFULLY !");
        if(!exceptions.isEmpty()){
            System.out.println("FOLLOWING ARE THE FILES THAT MAY SPELLED INCORRECTLY OR DO NOT EXIST IN THE DIRECTORY: ");
            exceptions.forEach((n) -> System.out.println(n));
        }
    }
    public static void rm(int lowerBound, int upperBound, List<File> allFiles){
        for(int i=0;i<allFiles.size();i++){
            if(i==lowerBound){
                i = upperBound;
                continue;
            }
            
            allFiles.get(i).delete();
        }
        System.out.println("FILES REMOVED SUCCESSFULLY !");
    }
    public static void rn_To(int i, int j, StringBuilder stringSeq, List<File> allFiles, String ext, File currentDirectory){
        
        Stack<File> files = (Stack<File>) allFiles;

        StringBuilder temp = new StringBuilder(stringSeq);
        Collections.reverse(files);

        while(!files.isEmpty()){

            temp.replace(temp.indexOf(";"), temp.indexOf(";")+1, String.valueOf(i));
            i++;

            if(j!=-1234){
            temp.replace(temp.indexOf("\\"), temp.indexOf("\\")+1, String.valueOf(j));
            j++;
            }

            temp.append(ext);

            files.pop().renameTo(new File(currentDirectory.getAbsolutePath()+"/"+temp.toString()));
            temp.replace(0, temp.length(), stringSeq.toString());
        }
    }
    public static void rn_self(List<File> fileList, StringBuilder ext, File currentDirectory){
        Stack<File> allFiles = (Stack<File>) fileList;

        StringBuilder newFileName = new StringBuilder();
        Collections.reverse(allFiles);

        Scanner in = new Scanner(System.in);
        File file;

        System.out.println("PLEASE PROVIDE THE NEW NAMES FOR THE FOLLOWING FILES:");
        System.out.println("[NOTE:THERE IS NO COMPULSION TO MENTION EXTENSION WHILE RENAMING FILES]\n");
        System.out.println("[NOTE:TYPE \"/\", IF YOU WISH TO SKIP A SPECIFIC FILE]");

        if(ext != null){
        while(!allFiles.isEmpty()){
            file = allFiles.pop();
            System.out.print(file.getName()+" ----RENAME_TO----> ");
            newFileName.replace(0, newFileName.length(), in.nextLine());

            if(newFileName.toString().equals("/"))
            continue;

            if(newFileName.toString().equals("")){
                System.out.println("NEW NAME FOR THE FILE CANNOT BE LEFT UNDEFINED!");
                System.exit(0);
            }

            try{
            if(newFileName.substring(newFileName.length()-ext.length()).equals(ext.toString())){
                file.renameTo(new File(currentDirectory.getAbsolutePath()+"/"+newFileName.toString()));
            }else{
                throw new IndexOutOfBoundsException();
            }
            }catch(IndexOutOfBoundsException e){
                file.renameTo(new File(currentDirectory.getAbsolutePath()+"/"+newFileName.toString()+ext.toString()));
            }
        }
        }else{
            ext = new StringBuilder();
            while(!allFiles.isEmpty()){
                file = allFiles.pop();

                for(int i=file.getName().length()-1;i>=0;i--){
                    if(file.getName().charAt(i) == '.'){
                        ext.replace(0, ext.length(), file.getName().substring(i));
                    }
                }

                System.out.print(file.getName()+" ----RENAME_TO----> ");
                newFileName.replace(0, newFileName.length(), in.nextLine());
    
                if(newFileName.toString().equals("")){
                    System.out.println("NEW NAME FOR THE FILE CANNOT BE LEFT UNDEFINED!");
                    System.exit(0);
                }
    
                try{
                if(newFileName.substring(newFileName.length()-ext.length()).equals(ext.toString())){
                    file.renameTo(new File(currentDirectory.getAbsolutePath()+"/"+newFileName.toString()));
                }else{
                    throw new IndexOutOfBoundsException();
                }
                }catch(IndexOutOfBoundsException e){
                    file.renameTo(new File(currentDirectory.getAbsolutePath()+"/"+newFileName.toString()+ext.toString()));
                }
            }   
        }
        in.close();
    }
}