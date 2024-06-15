package com.example.forumapp.Model;

import java.io.Serializable;

public class Domain implements Serializable {
    String did,DomainName;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getDomainName() {
        return DomainName;
    }

    public void setDomainName(String domainName) {
        DomainName = domainName;
    }

    @Override
    public String toString() {
        return DomainName;
    }
}
