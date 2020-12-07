package com.hogwartsmini.demo.dao;

import com.hogwartsmini.demo.common.MySqlExtensionMapper;
import com.hogwartsmini.demo.entity.HogwartsTestJenkins;
import org.springframework.stereotype.Repository;

@Repository //dao层注入容器的bean
public interface HogwartsTestJenkinsMapper extends MySqlExtensionMapper<HogwartsTestJenkins> {
}