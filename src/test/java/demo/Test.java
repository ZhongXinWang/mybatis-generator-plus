package demo;

public class Test {
    public static void main(String[] args) {

        String str= "1<Model, PK extends Serializable>.java";
        if(str.indexOf("<") != -1){

            str = str.substring(0,str.indexOf("<"))+".java";

            System.out.println(str);
        }
    }
}
