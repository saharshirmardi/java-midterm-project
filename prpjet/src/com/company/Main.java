package com.company;

import java.util.ArrayList;

import java.util.Scanner;

public class Main {

        static class Node{
                Node parent = null;
                String name = "";
                boolean used = false;
                public ArrayList<Node> or_child = new ArrayList<Node>();
                public ArrayList<Node> xor_child = new ArrayList<Node>();
                public ArrayList<Node> mand_child = new ArrayList<Node>();
                public ArrayList<Node> opt_child = new ArrayList<Node>();
        }

        static class List{
                public static ArrayList<Node> nodes = new ArrayList<Node>();
                public static ArrayList<String> ans = new ArrayList<String>();
        }

        public static void split_input (ArrayList<String> Arr){
                int split_index = 0;
                for(int i = 0; i < Arr.size(); i++)
                        if (Arr.get(i).equals("#")) {
                                split_index = i;
                                break;
                        }
                for (int i= 0 ; i<split_index ; i++)
                        parse1(Arr.get(i));
                for (int i = split_index+1; i < Arr.size(); i++)
                        parse2(Arr.get(i));
        }

        public static void parse1(String s1) {
                s1= s1.trim().replaceAll("\\s+", "");
                String parent, children;
                if (s1.contains("=")) {
                        int split_index = 0;
                        for(int i = 0; i < s1.length(); i++)
                                if (s1.charAt(i)=='=') {
                                        split_index = i;
                                        break;
                                }
                        parent = s1.substring(0,split_index);
                        children = s1.substring(split_index+1,s1.length());
                } else {
                        parent = s1;
                        children = "";
                }
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
                        if (defined == false) {
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

                else if (children.length() > 0) {
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

        public static void parse2(String s2){
                s2 = s2.trim().replaceAll("\\s+", "");
                s2 = s2.substring(1, s2.length() - 1);
                String[] test_case;
                test_case = s2.split(",", -1);
                check(test_case);
                for (int i = 0; i < List.nodes.size(); i++)
                        List.nodes.get(i).used = false;
        }

        public static void check(String[] test) {
                boolean valid = (test.length > 0);
                for (int i = 0; i < test.length; i++) {
                        boolean occurs = false;
                        for (int j = 0; j < List.nodes.size(); j++) {
                                if (test[i].equals(List.nodes.get(j).name)) {
                                        List.nodes.get(j).used = true;
                                        occurs = true;
                                        break;
                                }
                        }
                        valid &= occurs;
                }

                if (valid)
                        for (int i = 0; i < List.nodes.size(); i++) {
                                Node node = List.nodes.get(i);
                                if (node.used == true) {
                                        boolean par = check_par(node);
                                        boolean mand = check_mand(node);
                                        boolean or = check_or(node);
                                        boolean xor = check_xor(node);
                                        valid &= par && mand && or && xor;
                                }
                        }

                if (valid)
                        List.ans.add("Valid");
                else
                        List.ans.add("Invalid");
        }

        public static boolean check_par(Node p) {
                if (p.parent != null && p.parent.used == false)
                        return false;
                return true;
        }

        public static boolean check_mand(Node p) {
                if (p.mand_child.size() == 0)
                        return true;
                for (int i = 0; i < p.mand_child.size(); i++)
                        if (p.mand_child.get(i).used == false)
                                return false;
                return true;
        }

        public static boolean check_or(Node p) {
                if (p.or_child.size() == 0)
                        return true;
                int or_count = 0;
                for (int i = 0; i < p.or_child.size(); i++)
                        if (p.or_child.get(i).used == true)
                                or_count++;
                if (or_count == 0)
                        return false;
                return true;
        }

        public static boolean check_xor(Node p) {
                if (p.xor_child.size() == 0)
                        return true;
                int xor_count = 0;
                for (int i = 0; i < p.xor_child.size(); i++)
                        if (p.xor_child.get(i).used == true)
                                xor_count++;
                if (xor_count != 1)
                        return false;
                return true;
        }

        public static void main(String[] args) {
                Scanner myObj = new Scanner(System.in);
                ArrayList<String> list = new ArrayList<String>();
                String tmp = " ";

                while(!(tmp.equals("###"))) {
                        tmp = myObj.nextLine();
                        if (!(tmp.equals("##"))) {
                                list.add(tmp);
                        }
                        else {
                                split_input(list);
                                List.ans.add("+++");
                                List.nodes.removeAll(List.nodes);
                                list.removeAll(list);
                        }
                }

                for (int i = 0; i < List.ans.size(); i++)
                        System.out.println(List.ans.get(i));
        }
}