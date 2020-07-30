package com.jfkj.im.Bean;

import java.util.List;

public class CityPickerBean   {

    private DataBean data;
    private String msg;
    private int code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean {
        private List<AreasBean> areas;

        public List<AreasBean> getAreas() {
            return areas;
        }

        public void setAreas(List<AreasBean> areas) {
            this.areas = areas;
        }

        public static class AreasBean {
            /**
             * id : 2
             * name : 北京
             * parent_id : 1
             * is_hot : 0
             * children : [{"id":52,"name":"北京","parent_id":2,"is_hot":1,"children":[{"id":500,"name":"东城区","parent_id":52,"is_hot":0},{"id":501,"name":"西城区","parent_id":52,"is_hot":0},{"id":502,"name":"海淀区","parent_id":52,"is_hot":0},{"id":503,"name":"朝阳区","parent_id":52,"is_hot":0},{"id":504,"name":"崇文区","parent_id":52,"is_hot":0},{"id":505,"name":"宣武区","parent_id":52,"is_hot":0},{"id":506,"name":"丰台区","parent_id":52,"is_hot":0},{"id":507,"name":"石景山区","parent_id":52,"is_hot":0},{"id":508,"name":"房山区","parent_id":52,"is_hot":0},{"id":509,"name":"门头沟区","parent_id":52,"is_hot":0},{"id":510,"name":"通州区","parent_id":52,"is_hot":0},{"id":511,"name":"顺义区","parent_id":52,"is_hot":0},{"id":512,"name":"昌平区","parent_id":52,"is_hot":0},{"id":513,"name":"怀柔区","parent_id":52,"is_hot":0},{"id":514,"name":"平谷区","parent_id":52,"is_hot":0},{"id":515,"name":"大兴区","parent_id":52,"is_hot":0},{"id":516,"name":"密云县","parent_id":52,"is_hot":0},{"id":517,"name":"延庆县","parent_id":52,"is_hot":0}]}]
             */

            private int id;
            private String name;
            private int parent_id;
            private int is_hot;
            private List<ChildrenBeanX> children;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getParent_id() {
                return parent_id;
            }

            public void setParent_id(int parent_id) {
                this.parent_id = parent_id;
            }

            public int getIs_hot() {
                return is_hot;
            }

            public void setIs_hot(int is_hot) {
                this.is_hot = is_hot;
            }

            public List<ChildrenBeanX> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenBeanX> children) {
                this.children = children;
            }

            public static class ChildrenBeanX {
                /**
                 * id : 52
                 * name : 北京
                 * parent_id : 2
                 * is_hot : 1
                 * children : [{"id":500,"name":"东城区","parent_id":52,"is_hot":0},{"id":501,"name":"西城区","parent_id":52,"is_hot":0},{"id":502,"name":"海淀区","parent_id":52,"is_hot":0},{"id":503,"name":"朝阳区","parent_id":52,"is_hot":0},{"id":504,"name":"崇文区","parent_id":52,"is_hot":0},{"id":505,"name":"宣武区","parent_id":52,"is_hot":0},{"id":506,"name":"丰台区","parent_id":52,"is_hot":0},{"id":507,"name":"石景山区","parent_id":52,"is_hot":0},{"id":508,"name":"房山区","parent_id":52,"is_hot":0},{"id":509,"name":"门头沟区","parent_id":52,"is_hot":0},{"id":510,"name":"通州区","parent_id":52,"is_hot":0},{"id":511,"name":"顺义区","parent_id":52,"is_hot":0},{"id":512,"name":"昌平区","parent_id":52,"is_hot":0},{"id":513,"name":"怀柔区","parent_id":52,"is_hot":0},{"id":514,"name":"平谷区","parent_id":52,"is_hot":0},{"id":515,"name":"大兴区","parent_id":52,"is_hot":0},{"id":516,"name":"密云县","parent_id":52,"is_hot":0},{"id":517,"name":"延庆县","parent_id":52,"is_hot":0}]
                 */

                private int id;
                private String name;
                private int parent_id;
                private int is_hot;
                private List<ChildrenBean> children;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getParent_id() {
                    return parent_id;
                }

                public void setParent_id(int parent_id) {
                    this.parent_id = parent_id;
                }

                public int getIs_hot() {
                    return is_hot;
                }

                public void setIs_hot(int is_hot) {
                    this.is_hot = is_hot;
                }

                public List<ChildrenBean> getChildren() {
                    return children;
                }

                public void setChildren(List<ChildrenBean> children) {
                    this.children = children;
                }

                public static class ChildrenBean {
                    /**
                     * id : 500
                     * name : 东城区
                     * parent_id : 52
                     * is_hot : 0
                     */

                    private int id;
                    private String name;
                    private int parent_id;
                    private int is_hot;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public int getParent_id() {
                        return parent_id;
                    }

                    public void setParent_id(int parent_id) {
                        this.parent_id = parent_id;
                    }

                    public int getIs_hot() {
                        return is_hot;
                    }

                    public void setIs_hot(int is_hot) {
                        this.is_hot = is_hot;
                    }
                }
            }
        }
    }
}
