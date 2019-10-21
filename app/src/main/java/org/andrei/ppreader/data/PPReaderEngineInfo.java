package org.andrei.ppreader.data;

import java.io.Serializable;

public class PPReaderEngineInfo implements Serializable {

    private static final long serialVersionUID = -7431546053378881135L;
    public String name;
    public String contentUrl;
    public String imageUrl;
    public boolean isUsed = true;
    public transient boolean isSelected = false;

}
