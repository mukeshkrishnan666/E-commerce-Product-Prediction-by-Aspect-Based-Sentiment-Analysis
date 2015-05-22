package nlp.project;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
//import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;



public class ReviewManager 
{  
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        //SentimentClassifier sc = new SentimentClassifier();
        //sc.train();
        Scanner sc = new Scanner(System.in);
        System.out.println("What kind of camera you are looking for?");
        System.out.println("1. Digital 2.SLR");
        int camType = sc.nextInt();
        System.out.println("Which of the following aspects you are looking for?");
        System.out.println("1. Zoom 2.Picture Quality 3. Color 4. Weight 5. Noise Reduction 6. Point & Shoot 7. Memory 8. Battery 9. Control 10. Resolution");
        int feature = sc.nextInt();
        sc.close();
        String cam = "";
        if(camType==1)
            cam="digital";
        else
            cam="slr";
        String ass = "";
        String subass = "";
        if(feature==1)
            ass="zoom";
        else if(feature==2)
        {
            ass="picture";
            subass="pictures";
        }
        else if(feature==3)
        {
            ass="color";
            subass="colors";
        }
        else if(feature==4)
        {
            ass="weight";
        }
        else if(feature==5)
        {
            ass="noise";
            subass="reduction";
        }
        else if(feature==6)
        {
            ass="point-and-shoot";
        }
        else if(feature==7)
        {
            ass="memory";
            subass="card reader";
        }
        else if(feature==8)
        {
            ass="battery";
            subass="batteries";
        }
        else if(feature==9)
        {
            ass="control";
            subass="controls";
        }
        else if(feature==10)
        {
            ass="resolution";
            subass="quality";
        }
        BufferedReader br1 = null;
        String line1 = "";
        br1 = new BufferedReader(new FileReader("positive-words.txt"));
        ArrayList<String> posWords = new ArrayList<String>();
        ArrayList<String> negWords = new ArrayList<String>();
        while ((line1 = br1.readLine()) != null)
        {
            posWords.add(new String(line1));
        }
        BufferedReader br2 = null;
        String line2 = "";
        br2 = new BufferedReader(new FileReader("negative-words.txt"));
        while ((line2 = br2.readLine()) != null)
        {
            negWords.add(new String(line2));
        }
        
        BufferedReader br = null;
        String line = "";
        br = new BufferedReader(new FileReader("camera.txt"));
        ArrayList<Review> reviewAry = new ArrayList<Review>();
        ArrayList<Integer> resultAry = new ArrayList<Integer>();
        ArrayList<Integer> negresultAry = new ArrayList<Integer>();
        int flag = 0;
        String[] details = new String[5];
        int d = 0;
        while ((line = br.readLine()) != null)
        {
            
            if(flag<10)
            {
                if((flag<=1 || flag>=6) && flag!=7)
                {
                    StringTokenizer st = new StringTokenizer(line,":");
                    String temp = st.nextToken();
                    temp = st.nextToken();
                    details[d] = temp.toLowerCase();
                    ++d;
                    //System.out.println(line);
                }
                if(flag==9)
                {
                    d=0;
                    reviewAry.add(new Review(details));
                    details = new String[5];
                }
                flag++;
            }
            else
                flag = 0;
            
        }
        Tokenizer _tokenizer = null;
        InputStream modelIn = null;
        try 
        {
           // Loading tokenizer model
            modelIn = new FileInputStream("en-pos-maxent.bin");
            final POSModel posModel = new POSModel(modelIn);
            modelIn.close();
            PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
            POSTaggerME tagger = new POSTaggerME(posModel);
            ObjectStream<String> lineStream = new PlainTextByLineStream(new StringReader(reviewAry.get(0).text));
            InputStream is = new FileInputStream("en-token.bin");
            TokenizerModel model = new TokenizerModel(is);
            Tokenizer tokenizer = new TokenizerME(model);
            InputStream sent1 = new FileInputStream("en-sent.bin");
            SentenceModel smodel = new SentenceModel(sent1);
            SentenceDetectorME sdetector = new SentenceDetectorME(smodel);
            System.out.println("Fetching Results...");
            String[] negative_opinion={"ugly","horrible","old","little","wrong","loose","tight","low","major","significant","small","weak","bad","useless","confusing","weird","uncomfortable","poor","crackling","difficult","defective","worst","scratchy","crappy","slow","worthless","ok","tiny","noisy","clumsy","obsolete","outdated","out-dated","few","used","cheap","sorry","sweaty","flimsy","dead","intermittent","overpriced","ridiculous","short","invalid","expensive","terrible","fading","gimmicky","noisey","stiff","stupid","heavy","obnoxious","disappointing","rigid","questionable","cheapo","broken","non-standard","annoying","sloppy"};
            String[] positive_opinion={"standardized","standard","new","decent","good","enough","great","original","best","extra","first","nice","top","extraordinary","awesome","big","extended","slim","right","standard","multi-function","multifunction","fantastic","fresh","well","sexy","reputable","fine","portable","reasonable","perfect","clear","acceptable","inexpensive","worth","highest","favorite","thin","intuitive","speedy","prompt","amazing","viable","large","unmarked","steady","protective","neat","sound","soft","full-fledged","outstanding","lightweight","alright","sturdy","strong","happy","stylish","safe","instant","latest","cool"};
            for(int i=0;i<positive_opinion.length;i++)
            posWords.add(positive_opinion[i]);
            for(int i=0;i<negative_opinion.length;i++)
            negWords.add(negative_opinion[i]);

        for(int m=0;m<reviewAry.size();m++)
        {
           String sentences[] = sdetector.sentDetect(reviewAry.get(m).text);
           if(reviewAry.get(m).title.contains(cam))
           for(int i=0;i<sentences.length;i++)
           {
                String s[] = tokenizer.tokenize(sentences[i]);
                String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE.tokenize(sentences[i]);
                String[] tags = tagger.tag(whitespaceTokenizerLine);
                for(int j=0;j<tags.length;j++)
                {
                        if(ass.equals(whitespaceTokenizerLine[j].toLowerCase()) || subass.equals(whitespaceTokenizerLine[j].toLowerCase()))
                        {
                            //System.out.println("Aspect="+s[j]);
                            for(int k=0;k<tags.length;k++)
                            {
                                if(tags[k].contains("JJ"))
                                {
                                    //System.out.println("-----mila-----");
                                    if(posWords.contains(whitespaceTokenizerLine[k].toLowerCase()))
                                    {
                                        if(!resultAry.contains(m))
                                        {
                                           if(Double.parseDouble(reviewAry.get(m).rating)>=3.0)
                                           resultAry.add(m);
                                            //System.out.println(resultAry.size());
                                        }
                                    }
                                    else if(negWords.contains(whitespaceTokenizerLine[k].toLowerCase()))
                                    {
                                        if(!negresultAry.contains(m))
                                        {
                                           if(Double.parseDouble(reviewAry.get(m).rating)<3.0)
                                           negresultAry.add(m);
                                            //System.out.println(resultAry.size());
                                        }
                                    }
                                }
                            }
                        }
                    //}
                }
            }
        }
        //System.out.println(resultAry.size());
        if(resultAry.size()!=0)
        {
            System.out.println("Following products are best suitable for you:");
            for(int j10=0;j10<resultAry.size();j10++)
            {
                System.out.println("Result No : "+(j10+1));
                System.out.println("Product ID : "+reviewAry.get(resultAry.get(j10)).productID);
                System.out.println("Title : "+reviewAry.get(resultAry.get(j10)).title);
                System.out.println("Summary : "+reviewAry.get(resultAry.get(j10)).summary);
                System.out.println("Rating : "+reviewAry.get(resultAry.get(j10)).rating);
                System.out.println("Review : "+reviewAry.get(resultAry.get(j10)).text);
                System.out.println("");
            }
            System.out.println("");
            System.out.println("Total positive reviews = "+resultAry.size());
        }
        else
        {
            System.out.println("Sorry... No positive results found.");
        }
        System.out.println("");
        System.out.println("=========================================");
        System.out.println("");
        if(negresultAry.size()!=0)
        {
            System.out.println("Following products are not recommended by the users:");
            for(int j10=0;j10<negresultAry.size();j10++)
            {
                System.out.println("Result No : "+(j10+1));
                System.out.println("Product ID : "+reviewAry.get(negresultAry.get(j10)).productID);
                System.out.println("Title : "+reviewAry.get(negresultAry.get(j10)).title);
                System.out.println("Summary : "+reviewAry.get(negresultAry.get(j10)).summary);
                System.out.println("Rating : "+reviewAry.get(negresultAry.get(j10)).rating);
                System.out.println("Review : "+reviewAry.get(negresultAry.get(j10)).text);
                System.out.println("");
            }
            System.out.println("");
            System.out.println("Total negative reviews = "+negresultAry.size());
        }
        else
        {
            System.out.println("Hurray... No negative results found!");
        }

    } catch (final IOException ioe) 
    {
       ioe.printStackTrace();
    } finally {
       if (modelIn != null) {
          try {
             modelIn.close();
          } catch (final IOException e) {} // oh well!
       }
        }
    }
}