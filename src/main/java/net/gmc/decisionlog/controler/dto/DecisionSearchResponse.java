package net.gmc.decisionlog.controler.dto;


import net.gmc.decisionlog.model.Decision;

import java.util.List;

public class DecisionSearchResponse {

    public String nextScrollId;
    public List<Decision> decisions;

}
