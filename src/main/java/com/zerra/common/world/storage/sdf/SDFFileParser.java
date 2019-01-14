package com.zerra.common.world.storage.sdf;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SDFFileParser {
    private static final Pattern LINE_PARSER = Pattern.compile("^(\\s*)(\\w+):([a-zA-Z])(\\w*)$");

    public static SimpleDataFormat toSDF(List<String> stringLines) throws SDFParseException {
        int layer = 0;
        String lastKey = null;
        List<SimpleDataFormat> sdfList = new ArrayList<>();
        sdfList.add(new SimpleDataFormat());
        for (int i = 0; i < stringLines.size(); i++) {
            String line = stringLines.get(i);
            Matcher matcher = LINE_PARSER.matcher(line);

            //Validate data layer
            int layers = matcher.group(1).length();
            if (layers > layer) {
                //New data layer has unexpectedly been created
                throw new SDFParseException("Invalid data format! New layer started without data definition on line %s", i);
            } else if (layers < layer) {
                if (lastKey == null) {
                    throw new SDFParseException("Invalid data format! Couldn't close data layer on line %s due to no key on the line before!", i);
                }
                //Data layers have been closed - attach inner object to the next one up
                for (int l = layers; l < layer; l--) {
                    sdfList.get(l - 1).setData(lastKey, sdfList.get(l));
                    sdfList.remove(l);
                }
            }

            //Get key
            String key = matcher.group(2);
            if (StringUtils.isEmpty(key)) {
                throw new SDFParseException("Invalid data format! No key on line %s", i);
            }
            lastKey = key;

            //Get value type
            String type = matcher.group(3);
            if (StringUtils.isEmpty(type)) {
                throw new SDFParseException("Invalid data format! No value type on line %s", i);
            }

            //Get value
            String value = matcher.group(4);
            if (StringUtils.isEmpty(value)) {
                //Start of a layer
                layer++;
                sdfList.add(new SimpleDataFormat());
                continue;
            }

            Object object = SDFDataType.getInstanceFromString(type.charAt(0), value);
            if (object == null) {
                throw new SDFParseException("Invalid data format! The type was not valid or the value was not valid for the type on line %s", i);
            }

            sdfList.get(layer).set(key, object);
        }
        return sdfList.get(0);
    }

    public static List<String> fromSDF(SimpleDataFormat sdf) {
        List<String> lines = new LinkedList<>();

        return lines;
    }
}
