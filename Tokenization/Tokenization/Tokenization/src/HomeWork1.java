
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by miren_t on 1/29/2015.
 */
public class HomeWork1 {


    static int countOfFiles=0;
    static TreeMap<String, Integer> tokens = new TreeMap<String, Integer>();
    static TreeMap<String, Integer> stems = new TreeMap<String, Integer>();

    static int numberOfTokens=0;
    static int numberOfStems=0;
    static int avgFile=0;
    static int avgStemFile= 0;

    public static void main(String args[]){
         String filePath = "C:\\Workspace\\Github\\Information Retrieval\\Tokenization\\Tokenization\\Tokenization\\src\\C2\\";

        long startTime = Calendar.getInstance().getTimeInMillis();

        try {
            scanFiles(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

   /*     Iterator<Map.Entry<String, Integer>> entryIterator = tokens.entrySet().iterator();
        while(entryIterator.hasNext()) {
            Map.Entry<String, Integer> entry = entryIterator.next();
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
*/

        System.out.println("Number of tokens: " + numberOfTokens);
        System.out.println("Number of unique tokens: " + tokens.size());
        System.out.println("Number of tokens that occur only once: " + countOnes(tokens));
   //     System.out.println("Number of files: " + countOfFiles);
        System.out.println("Number of average tokens per document: " + numberOfTokens/countOfFiles);
        System.out.println("Number of average 'distinct' tokens per document: " + avgFile/countOfFiles);
        System.out.println("30 most frequent tokens: ");

        TreeMap<String, Integer> sortedTokens= sortDecreasing(tokens);
        Iterator<Map.Entry<String, Integer>> sortedIterator = sortedTokens.entrySet().iterator();
        int count=1;
        while(count<=30){
            Map.Entry<String, Integer> entry = sortedIterator.next();
            System.out.println(count + ". " + entry.getKey() + "\t" + entry.getValue());
            count++;
        }

        System.out.println("Time taken to acquire characteristics: " + (Calendar.getInstance().getTimeInMillis()-startTime) + "ms");
        System.out.println("**********************");

        System.out.println("Number of distinct stems: " + stems.size());
        System.out.println("Number of stems occurring only once: " + countOnes(stems));
        System.out.println("Number of average stems per document: " + numberOfStems/countOfFiles);
        System.out.println("Number of average 'distinct' stems per document: " + avgStemFile/countOfFiles);
        System.out.println("30 most frequent stems: ");

        TreeMap<String, Integer> sortedStemTokens= sortDecreasing(stems);
        Iterator<Map.Entry<String, Integer>> sortedStemIterator = sortedStemTokens.entrySet().iterator();
        count=1;
        while(count<=30){
            Map.Entry<String, Integer> entry = sortedStemIterator.next();
            System.out.println(count + ". " + entry.getKey() + "\t" + entry.getValue());
            count++;
        }

    }


    private static void scanFiles(String filePath) throws FileNotFoundException {
        File file = new File(filePath);

        File listOfFiles[] = file.listFiles();

        for(int i=0; i<listOfFiles.length; i++){
            if(listOfFiles[i].isFile()){
                countOfFiles++;
                    fetchWords(listOfFiles[i]);
            }

        }
    }

    static int countOnes(TreeMap<String, Integer> tokens){
        int countOfOnes=0;
        Iterator<Map.Entry<String, Integer>> countIterator = tokens.entrySet().iterator();
        while(countIterator.hasNext()){
            Map.Entry<String, Integer> entry = countIterator.next();
            if(entry.getValue()==1)
                countOfOnes++;
        }

        return countOfOnes;
    }

    public static TreeMap<String, Integer> sortDecreasing(final TreeMap<String, Integer> tokens){
        Comparator<String> stringComparator = new Comparator<String>() {
            public int compare(String o1, String o2) {
                if (tokens.get(o2).compareTo(tokens.get(o1)) == 0)
                    return 1;
                else
                    return tokens.get(o2).compareTo(tokens.get(o1));
            }
        };

        TreeMap<String, Integer> sortedTokens = new TreeMap<String, Integer>(stringComparator);
        sortedTokens.putAll(tokens);
        return sortedTokens;
    }

    static void fetchWords(File file) throws FileNotFoundException {
        Scanner inputFile = new Scanner(file);
        TreeMap<String, Integer> avgPerFile = new TreeMap<String, Integer>();
        TreeMap<String, Integer> avgStemPerFile= new TreeMap<String, Integer>();

        while(inputFile.hasNextLine()){

            String currentLine = inputFile.nextLine();
            System.out.println("Raw- "+currentLine);
            if(currentLine!=null && !(currentLine.contains("<") && currentLine.contains(">"))){
            	System.out.println("Processed:"+currentLine);
                currentLine = currentLine.replaceAll("[-]", " ");
                StringTokenizer stringTokenizer = new StringTokenizer(currentLine);
                while(stringTokenizer.hasMoreTokens()){
                    String currentToken = stringTokenizer.nextToken().toLowerCase();

                    String currentTokenModified = currentToken.replaceAll("[^a-zA-Z0-9]", "");

                    if(currentTokenModified.equals(""))
                        continue;
                    else{
                        //push tokens of entire collection into TreeMap here
                        numberOfTokens++;
                        if(tokens.get(currentTokenModified) == null)
                            tokens.put(currentTokenModified, 1);
                        else
                            tokens.put(currentTokenModified, tokens.get(currentTokenModified) + 1);

                        //count distinct tokens in each document here
                        if(avgPerFile.get(currentTokenModified) == null)
                            avgPerFile.put(currentTokenModified, 1);
                        else
                            avgPerFile.put(currentTokenModified, avgPerFile.get(currentTokenModified) + 1);

                        //find stems here
                        Stemmer stemmer= new Stemmer();
                        char stemChars[] = currentTokenModified.toCharArray();
                        stemmer.add(stemChars, stemChars.length);
                        stemmer.stem();

                        //push stems of entire collection into TreeMap here
                        numberOfStems++;
                        if(stems.get(stemmer.toString()) == null)
                            stems.put(stemmer.toString(), 1);
                        else
                            stems.put(stemmer.toString(), stems.get(stemmer.toString()) + 1);

                        //count distinct stems here
                        if(avgStemPerFile.get(stemmer.toString()) == null)
                            avgStemPerFile.put(stemmer.toString(), 1);
                        else
                            avgStemPerFile.put(stemmer.toString(), avgStemPerFile.get(stemmer.toString()) + 1);
                    }
                }
            }
        }
        avgFile+= avgPerFile.size();
        avgStemFile+= avgStemPerFile.size();
        avgPerFile.clear();
        avgStemPerFile.clear();
    }
}
