package org.andrei.ppreader.data;

import java.io.Serializable;

public class PPReaderEngineSetting implements Serializable {

    private static final long serialVersionUID = -7431546053378881135L;
    public String name;
    public transient String contentUrl;
    public transient String imageUrl;
    public boolean isUsed = true;
    public transient boolean isSelected = false;

}
