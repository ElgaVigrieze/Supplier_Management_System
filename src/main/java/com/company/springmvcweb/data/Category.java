package com.company.springmvcweb.data;

import java.util.*;

public enum Category {
    RAW_MATERIAL(CatValues.RAW_MATERIAL),
    FASTENERS(CatValues.FASTENERS),
    TURNING_MILLING(CatValues.TURNING_MILLING);

    public final String value;
    public static List<Category> categoriesPrivate = Arrays.asList(Category.values());

    private Category(String value){
        this.value = value;
    }

    private static final Map<String, Category> CAT_VALUES = new HashMap<>();

    static {
        for (Category c: values()) {
            CAT_VALUES.put(c.value, c);
        }
    }


    public static Category valueOfLabel(String label) {
        return  CAT_VALUES.get(label);
    }

    public String getLabel(){
        return this.value;
    }

    public static List<String>getCategoriesPublic() {
        List<String> categories = new ArrayList<>();
        for (Category c: categoriesPrivate)  {
            categories.add(c.getLabel());
        }
        return categories;
    }

    public static class CatValues {
        public static final String RAW_MATERIAL = "Raw material";
        public static final String FASTENERS="Fasteners";
        public static final String TURNING_MILLING="Turning/milling";

    }
}
