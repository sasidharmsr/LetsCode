package com.example.Let.sCode.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class statsResp {
    

    private List<Map<String,Object>> cfProblems;
    private List<Map<String,Object>> lcProblems;
    private List<Map<String,Object>> atProblems;

    public void setCfMap(List<Object[]> dbData){
        String query="contest_id,indicator,title,rating";
        String[] columns=query.split(",");
        cfProblems=new ArrayList<>();
        for(Object[] data:dbData){
            int index=0;
            Map<String,Object> cfmap=new HashMap<>();
            for(String column:columns){
                cfmap.put(column,data[index]);
                index++;
            }
            cfProblems.add(cfmap);
        }
    }

    public void setAtMap(List<Object[]> dbData){
        String query="unique_id,contest_id,problem_index,title";
        String[] columns=query.split(",");
        atProblems=new ArrayList<>();
        for(Object[] data:dbData){
            int index=0;
            Map<String,Object> atmap=new HashMap<>();
            for(String column:columns){
                atmap.put(column,data[index]);
                index++;
            }
            atProblems.add(atmap);
        }
    }

    public void setLcMap(List<Object[]> dbData){
        String query="id,path,title,difficulty,ac_rate";
        String[] columns=query.split(",");
        lcProblems=new ArrayList<>();
        for(Object[] data:dbData){
            int index=0;
            Map<String,Object> lcmap=new HashMap<>();
            for(String column:columns){
                lcmap.put(column,data[index]);
                index++;
            }
            lcProblems.add(lcmap);
        }
    }

}
