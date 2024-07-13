# mqtt-rpc-request

This project implements the requester part of Remote Procedure Call over mqtt (i.e request/response).
This may be useful in the case of a webapp which needs to subscribe to events, and also to handle requests that need a particular  response  

mqtt expects that a mosquitto broker is running to which clients connect and communicate using standard topics

The user of this project will write a Responder program as described below, and will provide a set of request handler classes 
which will be called process requests

It expects that a requests will be handles by a matching responder.


### Structure

A user written Requester program:

  * Make a RemoteProcedureCall instance
  * connects to an mqtt broker (this sets PRC as a callback adapter)
  * subscribe to the responseTopic (specific to this client)
  * generate a Request 
  * call rpc to publish the request
  * wait for the response to arrive
  * parse the response and display the result 


### Example

An example of the using mqtt-rpc to make a request is: [CalculatorRequest](https://github.com/rsmaxwell/mqtt-rpc-request/blob/main/src/test/java/com/rsmaxwell/mqtt/rpc/request/CalculatorRequest.java)


