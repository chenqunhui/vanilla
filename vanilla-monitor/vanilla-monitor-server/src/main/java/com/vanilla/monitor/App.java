package com.vanilla.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class App {
      public static void main(String [] args) {
        List<String> tempTxList = new ArrayList<String>();
        HashMap<Long,Long> map;
        tempTxList.add("a");
        tempTxList.add("b");
        tempTxList.add("c");
        tempTxList.add("d");
        tempTxList.add("e");

        MerkleTrees merkleTrees = new MerkleTrees(tempTxList);
        merkleTrees.merkle_tree();
        System.out.println("root : " + merkleTrees.getRoot());
      }
    }
