package vn.iotstar.models;

public class LoginResponse {
    private String token;
    private long expiresIn;

    public LoginResponse() {
    }

    public LoginResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public static class Builder {
        private String token;
        private long expiresIn;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder expiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public LoginResponse build() {
            return new LoginResponse(token, expiresIn);
        }
    }
}
