package com.ase.service;

import com.ase.model.Home;
import com.ase.model.HomeCategory;

import java.util.List;

public interface HomeService {

    Home creatHomePageData(List<HomeCategory> categories);

}
