package stepDefinitions;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "page",
        "pages",
        "per_page",
        "total",
        "sourceid",
        "sourcename",
        "lastupdated"
})

public class WBPojo {

    @JsonProperty("page")
    private Integer page;
    @JsonProperty("pages")
    private Integer pages;
    @JsonProperty("per_page")
    private Integer perPage;
    @JsonProperty("total")
    private Integer total;
    @JsonProperty("sourceid")
    private String sourceid;
    @JsonProperty("sourcename")
    private String sourcename;
    @JsonProperty("lastupdated")
    private String lastupdated;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("page")
    public Integer getPage() {
        return page;
    }

    @JsonProperty("page")
    public void setPage(Integer page) {
        this.page = page;
    }

    @JsonProperty("pages")
    public Integer getPages() {
        return pages;
    }

    @JsonProperty("pages")
    public void setPages(Integer pages) {
        this.pages = pages;
    }

    @JsonProperty("per_page")
    public Integer getPerPage() {
        return perPage;
    }

    @JsonProperty("per_page")
    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    @JsonProperty("total")
    public Integer getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(Integer total) {
        this.total = total;
    }

    @JsonProperty("sourceid")
    public String getSourceid() {
        return sourceid;
    }

    @JsonProperty("sourceid")
    public void setSourceid(String sourceid) {
        this.sourceid = sourceid;
    }

    @JsonProperty("sourcename")
    public String getSourcename() {
        return sourcename;
    }

    @JsonProperty("sourcename")
    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }

    @JsonProperty("lastupdated")
    public String getLastupdated() {
        return lastupdated;
    }

    @JsonProperty("lastupdated")
    public void setLastupdated(String lastupdated) {
        this.lastupdated = lastupdated;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
