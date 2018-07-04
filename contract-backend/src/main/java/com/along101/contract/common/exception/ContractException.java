package com.along101.contract.common.exception;

public class ContractException extends RuntimeException {
    public ContractException(String message) {
        super(message);
    }

    public ContractException(Throwable cause) {
        super(cause);
    }

    public ContractException(Throwable cause, String message) {
        super(message, cause);
    }

    public static ContractException newContractException(Throwable cause, String message, Object... params) {
        if (params != null && params.length > 0) {
            String format = String.format(message, params);
            return new ContractException(cause, format);
        } else {
            //todo
            return new ContractException(cause, message);
        }
    }

    public static ContractException newContractException(String message, Object... params) {
        if (params != null && params.length > 0) {
            String format = String.format(message, params);
            if (params[params.length - 1] instanceof Throwable) {
                return new ContractException((Throwable) params[params.length - 1], format);
            } else {
                return new ContractException(format);
            }
        } else {
            return new ContractException(message);
        }
    }
}
