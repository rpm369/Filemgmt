import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Filemgmt{
    static Scanner sc = new Scanner(System.in);

    static File currentDirectory = new File(Paths.get("").toAbsolutePath().toString());
    static String ext;
    static List<File> allFiles;
    static boolean args1IsPath;
    static HashSet<String> exceptionalStrings;
    static List<File> exceptionalFiles;

    static int seq_i = -1234;
    static int seq_j = -1234; 
    static StringBuilder seqString;

    static int lowerBoundIndex;
    static int upperBoundIndex;
    
    public static void main(String args[]){

        try{
            args1IsPath = Paths.get(args[1]).isAbsolute();
        }catch(IndexOutOfBoundsException e){
            System.out.println("INCOMPLETE COMMAND ERROR !");
            System.exit(0);
        }

        if(args1IsPath){
            currentDirectory = new File(args[1]);
            if(!(currentDirectory.exists() && currentDirectory.isDirectory())){
                System.out.println("THERE IS NO SUCH DIRECTORY AS "+currentDirectory.getAbsolutePath());
                System.exit(0);
            }
        }

        int index;

        if(args[0].equalsIgnoreCase("-rm")){

            if(args1IsPath)
            index = 2;
            else
            index = 1;

            
                try{

                 if(args[index].charAt(0)=='.' && args[index].length()<8){
                 ext = args[index];
                 }else{
                 System.out.println("THE EXTENSION FORMAT YOU HAVE PROVIDED IS INVALID, IN CASE OF PATH - DECLARE THE ABSOLUTE ONE");
                 System.exit(0);
                 }

                }catch(IndexOutOfBoundsException e){
                    System.out.println("NO EXTENSION IS DECLARED");
                    System.exit(0);
                }

                InitializeAllfiles();

                try{
                index++;
                if(args[index].equals("-e")){
                try{
                    exceptionalStrings = new HashSet<>();
                    index++;
                    for(int i=index;true;i++){

                        if(args[i].length()>ext.length() && args[i].substring(args[i].length()-ext.length()).equals(ext)){
                            exceptionalStrings.add(args[i]);
                        }else{
                            System.out.println("EXCEPTIONAL FILE NAMES MUST BE MENTIONED ALONG WITH THE PROPER EXTENSION");
                            System.exit(0);
                        }
                    }

                }catch(IndexOutOfBoundsException g){
                    if(exceptionalStrings.isEmpty()){
                        System.out.println("NO EXCEPTIONAL FILE HAS BEEN PROVIDED!");
                        System.exit(0);
                    }
                }

                PluginOperations.rm(exceptionalStrings, allFiles);

                }else if(args[index].equals("-er")){

                    try{
                    index++;
                    InitializeBounds(args[index]);
                    }catch(IndexOutOfBoundsException e){
                        System.out.println("NO RANGE HAS BEEN DECLARED !");
                        System.exit(0);
                    }

                    PluginOperations.rm(lowerBoundIndex, upperBoundIndex, allFiles);
                    
                }else if(args[index].equals("-ec")){
                    index++;
                    short initialFileLength = (short) allFiles.size();

                    try{
                    for(int i=0;i<allFiles.size();i++){
                     if(allFiles.get(i).getName().indexOf(args[index]) > -1){
                        allFiles.remove(i);
                        i--;
                     }
                    }
                    }catch(IndexOutOfBoundsException i){
                    System.out.println("NO KEYWORD OF EXCEPTIONAL FILE CONTENT IS GIVEN !");
                    System.exit(0);    
                    }

                    if(allFiles.size() == initialFileLength){
                    System.out.println("NO FILE FOUND WITH THE GIVEN KEYWORD, IT MIGHT HAVE MISSPELLED.");
                    System.out.print("MOVING FURTHUR IN THE PROGRAM WOULD DELETE EVERY FILE WITH THE SPECIFIED EXTENSION, WOULD YOU LIKE TO CONTINUE [Y/N]: ");
                    String response = sc.nextLine();

                    if(response.equalsIgnoreCase("N")){
                        System.exit(0);
                    }else if(response.equalsIgnoreCase("Y")){}
                    else{
                        System.out.println("INVALID RESPONSE!");
                        System.exit(0);
                    }
                    }
                    
                    PluginOperations.rm(allFiles);

                }else{
                    System.out.println("THERE ARE ONLY THREE POSSIBLE PLUGIN FOR REMOVE OPERATION i.e -e/-er/-ec");
                    System.exit(0);
                }

            }catch(IndexOutOfBoundsException e){
                PluginOperations.rm(allFiles);
            }  

        }else if(args[0].equalsIgnoreCase("-rn")){

            if(args1IsPath)
            index = 2;
            else
            index = 1;

            try{
            if(args[index].charAt(0) == '.' && args[index].length()<8){
                ext = args[index];
                InitializeAllfiles();
                index++;

                try{
                if(args[index].equalsIgnoreCase("-To")){
                    boolean selfStatus = false;
                    index++;

                    try{
                        InitializeStringSeq(args[index]);
                    }catch(IndexOutOfBoundsException g){
                        System.out.println("NO SEQUENCE FORMAT IS DETECTED.");
                        System.exit(0);
                    }

                    index++;
                    try{
                    if(args[index].equalsIgnoreCase("-e")){
                    exceptionalFiles = new Stack<>();
                    index++;
                    File tempFile;
                    try{
                        for(int i=index;true;i++){
                            if(args[i].equalsIgnoreCase("--self")){
                                selfStatus = true;
                                break;
                            }else if(args[i].charAt(0) == '-'){
                                System.out.println("ONLY \"--self\" PLUGIN IS APPLICABLE");
                                System.exit(0);
                            }else{  
                                for(int j=args[i].length()-1;j>=0;j--){
                                    if(args[i].charAt(j) == '.' && args[i].substring(j).equals(ext)){
                                        tempFile = new File(currentDirectory.getAbsolutePath()+"/"+args[i]);
                                        if(tempFile.exists()){
                                            exceptionalFiles.add(tempFile);
                                        }else{
                                            System.out.println("NO SUCH FILE EXIST AS "+args[i]+" FOR EXTENSION "+ext);
                                            System.exit(0);
                                        }
                                        break;
                                    }

                                    if(j==0){
                                        System.out.println("INVALID EXCEPTIONAL FILE: THE FILE MUST BE OF THE SAME EXTENSION");
                                        System.exit(0);
                                    }
                                }
                            }
                        }
                    }catch(IndexOutOfBoundsException r){}

                    if(exceptionalFiles.isEmpty()){
                        System.out.println("NO EXCEPTIONAL FILES GIVEN!");
                        System.exit(0);
                    }

                    allFiles.removeAll(exceptionalFiles);
                    PluginOperations.rn_To(seq_i, seq_j, seqString, allFiles, ext, currentDirectory);

                    if(selfStatus){
                        PluginOperations.rn_self(exceptionalFiles, new StringBuilder(ext), currentDirectory);
                    }

                    }else if(args[index].equalsIgnoreCase("-ec")){
                        exceptionalFiles = new Stack<>();
                        index++;

                        try{
                        for(int i=0;i<allFiles.size();i++){
                            if(allFiles.get(i).getName().indexOf(args[index]) > -1){
                                exceptionalFiles.add(allFiles.get(i));
                                allFiles.remove(i);
                                i--;
                            }
                        }
                        }catch(IndexOutOfBoundsException e){
                            System.out.println("NO KEYWORD OF CONTENT IS PROVIDED");
                            System.exit(0);
                        }

                        if(exceptionalFiles.isEmpty()){
                        System.out.println("NO FILE FOUND WITH THE GIVEN KEYWORD, IT MIGHT HAVE MISSPELLED.");
                        System.out.print("MOVING FURTHUR THE PROGRAM WOULD RENAME EVERY FILE WITH THE SPECIFIED EXTENSION, WOULD YOU LIKE TO CONTINUE [Y/N]: ");
                        String response = sc.nextLine();

                        if(response.equalsIgnoreCase("N")){
                        System.exit(0);
                        }else if(response.equalsIgnoreCase("Y")){}
                        else{
                        System.out.println("INVALID RESPONSE!");
                        System.exit(0);
                        }
                        }

                        PluginOperations.rn_To(seq_i, seq_j, seqString, allFiles, ext, currentDirectory);

                        index++;
                        try{
                        if(args[index].equalsIgnoreCase("--self")){
                            PluginOperations.rn_self(exceptionalFiles, new StringBuilder(ext), currentDirectory);
                        }else{
                            System.out.println("INVALID PLUGIN !");
                            System.exit(0);
                        }
                        }catch(IndexOutOfBoundsException e){}

                    }else if(args[index].equalsIgnoreCase("-er")){

                        try{
                        index++;
                        InitializeBounds(args[index]);
                        }catch(IndexOutOfBoundsException e){
                            System.out.println("NO RANGE HAS BEEN DECLARED !");
                            System.exit(0);
                        }
                    
                        exceptionalFiles = new Stack<>();
                        while(lowerBoundIndex <= upperBoundIndex){
                            exceptionalFiles.add(allFiles.get(lowerBoundIndex));
                            allFiles.remove(lowerBoundIndex);
                            upperBoundIndex--;
                        }

                        PluginOperations.rn_To(seq_i, seq_j, seqString, allFiles, ext, currentDirectory);

                        try{
                            if(args[index+1].equalsIgnoreCase("--self")){
                                PluginOperations.rn_self(exceptionalFiles, new StringBuilder(ext), currentDirectory);
                            }else{
                                System.out.println("NO OTHER PLUGIN IS ALLOWED HERE OTHER THAN \"--self\"");
                                System.exit(0);
                            }
                        }catch(IndexOutOfBoundsException e){}

                    }else{
                        System.out.println("NO SUCH PLUGIN EXISTS AS "+args[index]);
                        System.exit(0);
                    }

                }catch(IndexOutOfBoundsException k){
                    PluginOperations.rn_To(seq_i, seq_j, seqString, allFiles, ext, currentDirectory);
                }

                }else if(args[index].equalsIgnoreCase("--self")){
                    PluginOperations.rn_self(allFiles, new StringBuilder(ext), currentDirectory);
                }else{
                    System.out.println("INVALID PLUGIN !");
                    System.exit(0);
                }

                }catch(IndexOutOfBoundsException e){
                System.out.println("NO VALID PLUGIN FOUND OVER THE COMMON FILES EXTENSION TYPE.");
                System.exit(0);
                }

            }else{
                
                allFiles = new Stack<>();
                boolean hasSameExtension = true;
                boolean outerLoopState = true;
                File tempFile;

                try{
                for(int i=index;outerLoopState;i++){
                for(int j=args[i].length()-1;j>=0;j--){
                    if(args[i].charAt(j) == '.' && args[i].substring(j).length()<8){

                        if(i==index){
                            ext = args[i].substring(j);
                        }else if(!args[i].substring(j).equals(ext) && hasSameExtension == true){
                            hasSameExtension = false;
                        }

                        tempFile = new File(currentDirectory.getAbsolutePath()+"/"+args[i]);
                        if(!tempFile.exists()){
                            System.out.println("NO SUCH FILE AS "+args[i]+" EXISTS IN THE GIVEN DIRECTORY.");
                            System.exit(0);
                        }
                        if(!allFiles.contains(tempFile))
                        allFiles.add(tempFile);
                        break;
                    }

                    if(j==0 && args[i].charAt(j) !='-'){
                        System.out.println("THE PROPER FILE NAME MUST BE GIVEN ALONG WITH ITS EXTENSION.");
                        System.exit(0);
                    }else if(j==0 && args[i].charAt(j) == '-'){
                        index = i;
                        outerLoopState = false;
                        break;
                    }
                }
              }
            }catch(IndexOutOfBoundsException e){System.out.println("PROVIDE SOME PLUGIN!"); System.exit(0);}

            if(allFiles.isEmpty()){
                System.out.println("NO FILES HAVE BEEN PASSED TO BE RENAMED");
                System.exit(0);
            }
            
            if(args[index].equalsIgnoreCase("-To") && hasSameExtension == true){
                index++;

                try{
                InitializeStringSeq(args[index]);
                }catch(IndexOutOfBoundsException e){
                    System.out.println("NO SEQUENCE FORMAT HAS BEEN PROVIDED!");
                    System.exit(0);
                }

                PluginOperations.rn_To(seq_i, seq_j, seqString, allFiles, ext, currentDirectory);

            }else if(args[index].equalsIgnoreCase("--self")){
                if(hasSameExtension){
                    PluginOperations.rn_self(allFiles, new StringBuilder(ext), currentDirectory);
                }else{
                    PluginOperations.rn_self(allFiles, null, currentDirectory);
                }
            }else{
                System.out.println("THE CORRECT PLUGINS ARE \"-To\" and \"--self\", WHEREAS -To IS ONLY APPLICABLE WITH FILES WITH SIMILAR EXTENSIONS!");
                System.exit(0);
            }

            }

        }catch(IndexOutOfBoundsException e){
            System.out.println("NEITHER A SINGLE FILE NOR ANY EXTENSION IS FOUND !");
            System.exit(0);
        }

        }else{
            System.out.println("\"filemgmt\" ONLY SUPPORTS REMOVE(-rm), RENAME(-rn) FUNCTIONALITIES");
            System.exit(0);
        }

    } //change in function;

    public static void InitializeAllfiles(){
        List<File> list = Arrays.stream(currentDirectory.listFiles()).filter((file) -> {
            return (file.isFile() && file.getName().substring(file.getName().length() - ext.length()).equals(ext));
        }).toList();

        if(list.isEmpty()){
            System.out.println("THE DIRECTORY DOES NOT HOLD ANY FILE WITH EXTENSION AS \""+ext+"\"");
            System.exit(0);
        }else{
            allFiles = new Stack<>();
            allFiles.addAll((list.stream()).sorted(Custom.com).toList());
        }
    }
    public static void InitializeStringSeq(String userString){
        seqString = new StringBuilder(userString);

        if(seqString.length()<3){
            System.out.println("THE SEQUENCE FORMAT IS INVALID !");
            System.exit(0);
        }
        
        int tempNum = 0;
        for(int i=0;i<seqString.length();i++){
            if(seqString.charAt(i) == '/' && i!=seqString.length()-1){
                while(seqString.charAt(i+1) != '/'){
                    if(!Character.isDigit(seqString.charAt(i+1))){
                        System.out.println("NO SEQUENCE OTHER THAN NUMERICAL ONE IS ALLOWED!");
                        System.exit(0);
                    }
                    tempNum *= 10;
                    tempNum += seqString.charAt(i+1)-'0';
                    seqString.deleteCharAt(i+1);
                    if(i+1>seqString.length()){
                        System.out.println("NO TERMINATING PARAMETER FOUND !");
                        System.exit(i);
                    }
                }

                seqString.deleteCharAt(i+1);
                if(tempNum == 0){
                    System.out.println("INITIAL OF THE SEQUENCE CANNOT BE 0 OR LEFT NULL");
                    System.exit(0);
                }

                if(seq_i == -1234){
                    seq_i = tempNum;
                    seqString.replace(i, i+1, ";");
                }else if(seq_j == -1234){
                    seq_j = tempNum;
                    seqString.replace(i, i+1, "\\");
                }else{
                    System.out.println("NO MORE THAN ONE PARAMETER ARE ALLOWED TO BE MODIFIED !");
                    System.exit((0));
                }
                tempNum = 0;
            }else if(seqString.charAt(i) == '/' && i==seqString.length()-1){
                System.out.println("INVALID PARAMETERS DEFINATION!");    
                System.exit(0);
            }
        }

        if(seq_i == -1234){
            System.out.println("THERE MUST ATLEAST BE ONE SEQUENCE STYLE CONFIGURED.");
            System.exit(0);
        }

    }

    public static void InitializeBounds(String temp){
        try{
            String lowerBound = temp.substring(0, temp.indexOf("/"));
            String upperBound = temp.substring(temp.indexOf("/")+1);

            Stream<File> stream1 = allFiles.stream();
            Stream<File> stream2 = allFiles.stream();
            
            Predicate<File> match1 = file -> {
                if(file.getName().equals(lowerBound)){
                    lowerBoundIndex = allFiles.indexOf(file);
                    return true;
                }
                return false;
            };
            Predicate<File> match2 = file -> {
                if(file.getName().equals(upperBound)){
                    upperBoundIndex = allFiles.indexOf(file);
                    return true;
                }
                return false;
            };

            if(!(stream1.anyMatch(match1) && stream2.anyMatch(match2))){
            throw (new StringIndexOutOfBoundsException());
            }

            if(lowerBoundIndex > upperBoundIndex){
                System.out.println("IN THE GIVEN RANGE, FIRST FILE IS THE SECOND END POINT WHILE SECOND ONE THE FIRST END POINT. PLEASE SPECIFY THE RANGE CORRECTLY !");
                System.exit(0);
            }

            }catch(StringIndexOutOfBoundsException e){
                System.out.println("THE FORMAT OF RANGE IS INVALID OR THE FIRST AND THE LAST FILE MENTIONED, DOES NOT EXIST IN THE DIRECTORY.");
                System.exit(0);
            }
    }
}