package com.company;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;



public class Main {

    static class Node{
        Node parent = null;
        String name = "";
        public static  ArrayList<Node> or_child = new ArrayList<Node>();
        public static ArrayList<Node> xor_child = new ArrayList<Node>();
        public static ArrayList<Node> mand_child = new ArrayList<Node>();
        public static ArrayList<Node> opt_child = new ArrayList<Node>();
    }


    static class List{
        public static ArrayList<Node> nodes = new ArrayList<Node>();
    }


    static class Test{
        String s;
        String[] test;
        int i;
        Test(String s,String[] test,int i){
            this.s=s;
            this.test=test;
            this.i=i;
        }

        public boolean parent_test(){
            String node = s;
            boolean valid = false;
            for (int i = 0; i < List.nodes.size(); i++) {
                if (List.nodes.get(i).name.equals(node) && !(List.nodes.get(i).parent == null)) {
                    Node parent_name = List.nodes.get(i).parent;
                    for (int j = 0; j < test.length; j++) {
                        if (test[j].equals(parent_name.name)) {
                            valid = true;
                            break;
                        }
                    }
                } else if (List.nodes.get(i).name.equals(node) && List.nodes.get(i).parent == null) {
                    valid = true;
                }
            }
            return valid;

        }

        public boolean mand_test(){
            boolean valid = true;
            for(int j=0;j<Node.mand_child.size(); j++){
                if(Node.mand_child.size()>0 &&!(Arrays.asList(test).contains(Node.mand_child.get(j).name))){
                    valid = false;
                    break;
                }
                else {
                    valid = true;
                }
            }
            return valid;
        }

        public boolean or_test(){
            boolean valid = false;
            for(int j=0;j<Node.or_child.size(); j++){
                if(Node.or_child.size()>0 && (Arrays.asList(test).contains(Node.or_child.get(j).name))){
                    valid = true;
                    break;
                }
            }
            return valid;
        }

        public int xor_test(){
            int count = 0;
            for(int j=0;j<Node.xor_child.size(); j++){
                if(Node.xor_child.size()>0 && (Arrays.asList(test).contains(Node.xor_child.get(j).name))){
                    count++;
                }
            }
            return count;
        }
    }
    public static String tester(String[] test){
        boolean valid = false;
        boolean valid_1 = false;
        boolean valid_2 = false;
        boolean valid_3 = false;
        int count = 0;
        ArrayList<String> validity = new ArrayList<String>();
        for(int i=0; i<test.length; i++){
            if(List.nodes.get(i).name.equals(test[i])){
                Test t = new Test(test[i],test,i);
                valid_1 = t.parent_test();
                valid_2 = t.mand_test();
                valid_3 = t.or_test();
                count = t.xor_test();
            }
        }

        for(int i=0; i<test.length; i++){
            if(valid_1 && valid_2 && valid_3 && count == 1){
                validity.add("1");
            }
            else{
                validity.add("0");
            }
        }

        if(!(validity.contains("0"))){
            return "Valid";
        }
        else {
            return "Invalid";
        }
    }

    public static void parse1(String s1){

        s1= s1.trim().replaceAll("\\s+", "");
        String parent, children;
        int split_index = 0;
        for(int i = 0; i < s1.length(); i++)
            if (s1.charAt(i)=='=') {
                split_index = i;
                break;
            }
        parent = s1.substring(0,split_index);
        children = s1.substring(split_index+1,s1.length());
        Node P = new Node();
        if (List.nodes.size() == 0) {
            P.name = parent;
            P.parent = null;
            List.nodes.add(P);
        }
        else {
            boolean defined = false;
            for (int i = 0; i < List.nodes.size(); i++) {
                if (List.nodes.get(i).name.equals(parent)) {
                    defined = true;
                    P = List.nodes.get(i);
                    break;
                }
            }
            if(defined == false){
                P.name = parent;
                P.parent = null;
                List.nodes.add(P);
            }
        }



        if (children.contains("+")){
            String[] mand = new String[0];
            mand = children.split("\\+", 0);
            for ( int i = 0; i<mand.length ; i++) {
                boolean defined = false;
                for (int j = 0; j < List.nodes.size(); j++) {
                    if (List.nodes.get(j).name.equals(mand[i])) {
                        defined = true;
                        List.nodes.get(j).parent = P;
                        P.mand_child.add(List.nodes.get(j));
                        break;
                    }

                    else if (List.nodes.get(j).name.equals("?"+mand[i])) {
                        defined = true;
                        List.nodes.get(j).parent = P;
                        P.opt_child.add(List.nodes.get(j));
                        break;
                    }
                }
                if (defined == false) {
                    if (mand[i].charAt(0) == '?') {
                        Node C = new Node();
                        C.parent = P;
                        C.name = mand[i].substring(1, mand[i].length());
                        P.opt_child.add(C);
                        List.nodes.add(C);
                    }
                    else {
                        Node C = new Node();
                        C.parent = P;
                        C.name = mand[i];
                        P.mand_child.add(C);
                        List.nodes.add(C);
                    }
                }
            }
        }

        else if (children.contains("|")){
            String[] or = new String[0];
            or = children.split("\\|", 0);
            for ( int i = 0; i< or.length ; i++){
                boolean defined = false;
                for (int j = 0; j < List.nodes.size(); j++) {
                    if (List.nodes.get(j).name.equals(or[i])) {
                        defined = true;
                        List.nodes.get(j).parent = P;
                        P.or_child.add(List.nodes.get(j));
                        break;
                    }
                }
                if (defined == false) {
                    Node C = new Node();
                    C.parent = P;
                    C.name = or[i];
                    P.or_child.add(C);
                    List.nodes.add(C);
                }
            }
        }

        else if (children.contains("^")){
            String[] xor = new String[0];
            xor = children.split("\\^", 0);
            for ( int i = 0; i<xor.length ; i++) {
                boolean defined = false;
                for (int j = 0; j < List.nodes.size(); j++) {
                    if (List.nodes.get(j).name.equals(xor[i])) {
                        defined = true;
                        List.nodes.get(j).parent = P;
                        P.xor_child.add(List.nodes.get(j));
                        break;
                    }
                }
                if (defined == false) {
                    Node C = new Node();
                    C.parent = P;
                    C.name = xor[i];
                    P.xor_child.add(C);
                    List.nodes.add(C);
                }
            }
        }

        else {
            boolean defined = false;
            for (int i = 0; i < List.nodes.size(); i++) {
                if (List.nodes.get(i).name.equals(children)) {
                    defined = true;
                    List.nodes.get(i).parent = P;
                    P.mand_child.add(List.nodes.get(i));
                    break;
                }
                if (("?" + List.nodes.get(i).name).equals(children)) {
                    defined = true;
                    List.nodes.get(i).parent = P;
                    P.opt_child.add(List.nodes.get(i));
                    break;
                }
            }

            if (defined == false) {
                if (children.charAt(0) == '?') {
                    Node C = new Node();
                    C.parent = P;
                    C.name = children.substring(1, children.length());
                    P.opt_child.add(C);
                    List.nodes.add(C);
                }
                else {
                    Node C = new Node();
                    C.parent = P;
                    C.name = children;
                    P.mand_child.add(C);
                    List.nodes.add(C);
                }
            }
        }
    }

    public static String parse2(String s2){

        s2 = s2.trim().replaceAll("\\s+", "");
        s2 = s2.substring(1, s2.length() - 1);
        String[] test_case;
        test_case = s2.split(",", -1);
        return tester( test_case );
    }

    public static ArrayList<String> split_input (ArrayList<String> Arr){
        int split_index = 0;
        for(int i = 0; i < Arr.size(); i++)
            if (Arr.get(i).equals("#")) {
                split_index = i;
                break;
            }
        for (int i= 0 ; i<split_index ; i++)
            parse1(Arr.get(i));
        ArrayList<String> result = new ArrayList<>();
        for (int i = split_index+1; i < Arr.size(); i++)
            result.add(parse2(Arr.get(i)));
        result.add("+++");
        return result;
    }

    public static void main(String[] args) {


        Scanner myObj = new Scanner(System.in);
        ArrayList<String> list = new ArrayList<String>();
        String tmp = " ";

        ArrayList<String> result = new ArrayList<>();

        while(!(tmp.equals("###"))) {
            tmp = myObj.nextLine();
            if (!(tmp.equals("##"))) {
                list.add(tmp);
            }
            else {
                result.addAll(split_input(list));
                List.nodes.removeAll(List.nodes);
                list.removeAll(list);
            }
        }

        for (String line : result)
            System.out.println(line);
    }
}