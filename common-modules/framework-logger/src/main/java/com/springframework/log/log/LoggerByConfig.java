package com.springframework.log.log;

import com.google.common.base.Preconditions;
import com.springframework.utils.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 夏 on 2016/10/16
 *
 * @author Fisher
 */
public class LoggerByConfig {
    private org.slf4j.Logger logger;
    private Pattern projectNamePattern = Pattern.compile("[a-z0-9-.]+");
    private String dataId;

    public LoggerByConfig(String projectName, Class<?> clazz) {
        this.initial(projectName);
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public LoggerByConfig(String projectName, String name) {
        this.initial(projectName);
        this.logger = LoggerFactory.getLogger(name);
    }

    public void log(String messageFormat, Object... params) {
        String message = StringUtil.format(messageFormat, params);
        this.log(message);
    }

    public void log(String message) {
       /* String config = ConfigManager.getInstance().getConfig("COMMON", this.dataId);

        if (!StringUtils.equals(config, "1")) {
            return;
        }*/

        this.logger.info(message);
    }

    private void initial(String projectName) {
        Matcher matcher = projectNamePattern.matcher(projectName);
        Preconditions.checkArgument(matcher.matches(), "项目名称不正确");
        this.dataId = StringUtil.format("log-by-config-{0}", projectName);
    }
}
