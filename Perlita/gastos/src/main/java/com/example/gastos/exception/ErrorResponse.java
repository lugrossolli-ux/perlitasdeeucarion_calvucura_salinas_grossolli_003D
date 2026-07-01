package com.example.gastos.exception;

public class ErrorResponse {
    private int status;
    private String error;
    private String mensaje;
    private long timestamp;

    public ErrorResponse(int status, String error, String mensaje) {
        this.status = status;
        this.error = error;
        this.mensaje = mensaje;
        this.timestamp = System.currentTimeMillis();
    }

    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMensaje() { return mensaje; }
    public long getTimestamp() { return timestamp; }
}
