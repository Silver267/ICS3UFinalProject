import greenfoot.*;
/**
 * Engine which stores static variables and methods.
 * 
 * @author Xuanxi Jiang
 * @version 1.0
 */
public class Engine {
    public static final int worldWidth = 1600, worldHeight = 900;
    //world width and height.
    public static final int midW = worldWidth/2 - 200;
    //Simplifies code -- for stuff which needs to be initialized at "middle" with the exsistance of information panel.
    public static int WorldState = 2;
    // 2 - Battle stage 2; 3 - Ending;
    
    private static int getStringWidth (Font font, String text){//Borrowed & optimized code from Mr. Cohen to detect length of String.
        //Note: this method is optimized using binary search by me for better performance.
        int maxWidth = (int)(text.length() * (font.getSize()/1.20)), fontSize = font.getSize();
        int margin = fontSize/6, l = 0, r = maxWidth; //Skipped pixel; variables for binary search algorithm;
        GreenfootImage temp = new GreenfootImage (maxWidth+fontSize, fontSize);
        temp.setFont(font);
        temp.drawString (text, 0, fontSize);
        while(l<r){
            boolean found = false;
            int mid = (l+r)/2; //find middle (for binary search)
            //searching through a fontsize*fontsize space to find if font exsist
            for(int j=0; j<=fontSize-1 && !found; j+=margin)
                for (int i=0; i<=fontSize-1 && !found; i+=margin)
                    if (temp.getColorAt(mid+j, i).getAlpha() != 0)
                        found = true;
            if (found) //binary search case 1 (index too less)
                l=mid+1;
            else //binary search case 2 (index too much)
                r=mid;
        }
        return l;
    }
    
    public static String wordWrap(String in, Font f, int mxLength){//Method to convert a string to an automatically wraped string.
        int ind = 1; //current index
        String curWord = "", cur = in.charAt(0)+"", out = ""; //Initial strings
        while(ind<in.length()){ //Iterate through the text until the entire text is checked
            while(getStringWidth(f, cur)<mxLength){ //prevent overflow
                if(ind>=in.length()){//if index is more than the text size, break immediately
                    cur += curWord;
                    break;
                }
                if(in.charAt(ind)==' '){ //detect word
                    if(getStringWidth(f, cur+curWord)>=mxLength){//additional detection of overflow
                        ind -= curWord.length()-1;//if overflow, go back to the previous word (not including the "space" in [0]).
                        break;//break out of while loop
                    }
                    cur += curWord;//add current word to current string
                    curWord = "";//reset current word
                }else if(in.charAt(ind)=='\n'){//additional detection of new line
                    ind++; //if new line, go to next character and break the loop.
                    break;
                }
                curWord += in.charAt(ind);//add next letter to current word
                ind++;
            }
            out+=cur+"\n";//add current string to output string, and add an "\n" to represent next line.
            if(ind<in.length()){//if index is less than the text, continue to iterate.
                cur = in.charAt(ind)+""; 
                curWord = "";
                ind++;
            }
        }
        return out;
    }
}
