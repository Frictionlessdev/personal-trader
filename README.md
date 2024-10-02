# personal-trader

```markdown
### HTTP Status Codes and Example Responses

| **HTTP Status** | **Description**                             | **Example Response**                                                                                                      |
|-----------------|---------------------------------------------|---------------------------------------------------------------------------------------------------------------------------|
| **400**         | Generic Bad Request                         | ```json { "status": 400, "message": null, "error_code": "PM_OPEN_API_400" } ```                                            |
| **400**         | Error with Generic Message                  | ```json { "status": "error", "message": "Oops! Something went wrong.", "error_code": "400" } ```                           |
| **400**         | Invalid Security Id                         | ```json { "status": "error", "message": "Oops! Something went wrong", "data": [{ "oms_error_code": "3308" }], "error_code": "RS-0022" } ``` |
| **400**         | Invalid Exchange Value                      | ```json { "status": "error", "message": "Invalid Exchange value", "error_code": "400 BAD_REQUEST" } ```                    |
| **400**         | Invalid Segment and Exchange Value          | ```json { "status": "error", "message": "Invalid Segment value, Invalid Exchange value", "error_code": "400 BAD_REQUEST" } ``` |
| **400**         | Invalid Product Type                        | ```json { "status": "error", "message": "Invalid Product Type", "error_code": "PM-0006" } ```                              |
| **400**         | Invalid Source                              | ```json { "status": "error", "message": "Invalid Source", "error_code": "RS-9112" } ```                                    |
| **400**         | Token Request Error (Request Token Expired) | ```json { "status": 400, "error_code": "PM_MERCHANT_AUTH_400607", "message": "Invalid Api Key or Api Secret Key or Request Token" } ``` |

### 200 OK Responses

| **HTTP Status** | **Description**                              | **Example Response**                                                                                                 |
|-----------------|----------------------------------------------|----------------------------------------------------------------------------------------------------------------------|
| **200**         | Market Closed (off_mkt_flag = true)           | ```json { "status": "error", "message": "Market Is Closed now. Please retry to place order.", "data": [{ "order_no": "1871240924100", "oms_error_code": "O3052" }], "error_code": "RS-0022" } ``` |
| **200**         | Order Submitted Successfully                  | ```json { "status": "success", "message": "Order submitted successfully. Your Order Ref No. 1152409247150", "data": [{ "order_no": "1152409247150" }], "error_code": "RS-0023" } ``` |
```