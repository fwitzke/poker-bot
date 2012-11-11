package br.poker.bot.input;

public class TextProcessor {
    public String getDiff(String s1, String s2) {
        if(s2 == null || s2.equals(s1))
            return null;

        if(s1 == null)
            return s2;
        
        //String[] array1 = s1.split("\n");
        String[] array2 = s2.split("\n");

        String partial="";
        for(int index=0; index < array2.length; index++) {
            partial += array2[index] + "\n";

            if(s1.contains(partial))
                continue;
            else { 
                //This text is different
                String diff="";
                for(int x=index; x < array2.length; x++) {
                    diff += array2[x];

                    //Only puts '\n' where string was split
                    if( (x+1) < array2.length )
                        diff += "\n";
                }

                return diff;
            }
        }

        return null;
    }
    

}
