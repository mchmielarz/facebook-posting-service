package pl.devthoughts.facebook.client;

import com.restfb.Parameter;

import java.util.List;

public class PostData {

    private final List<Parameter> params;

    public PostData(List<Parameter> params) {
        this.params = params;
    }

    public Parameter[] getParams() {
        return params.toArray(new Parameter[params.size()]);
    }

}
