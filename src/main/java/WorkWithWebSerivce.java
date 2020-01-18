import javax.xml.soap.*;


class WorkWithWebSerivce {

    public static void main(String[] args) {
        /*
            The example below requests from the Web Service at:
             http://www.webservicex.net/uszip.asmx?op=GetInfoByCity
            To call other WS, change the parameters below, which are:
             - the SOAP Endpoint URL (that is, where the service is responding from)
             - the SOAP Action
            Also change the contents of the method createSoapEnvelope() in this class. It constructs
             the inner part of the SOAP envelope that is actually sent.
        http://www.thomas-bayer.com/axis2/services/BLZService?wsdl
         */

        String soapEndpointUrl = "http://www.thomas-bayer.com/axis2/services/BLZService?wsdl";
        String soapAction = "getBankResponse";
        int bankLeitZahl = 12030000;

        callSoapWebService(soapEndpointUrl, soapAction, bankLeitZahl);

    }

    private static void createSoapEnvelope(SOAPMessage soapMessage, int bankLeitZahl) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String myNamespace = "blz";
        String myNamespaceURI = "http://thomas-bayer.com/blz/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

        // SOAP Body
        // Fuellen des Body mit Werten für den Request
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("getBank", myNamespace);
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("blz", myNamespace);
        soapBodyElem1.addTextNode(Integer.toString(bankLeitZahl));

    }

    private static void callSoapWebService(String soapEndpointUrl, String soapAction, int bankLeitZahl) {
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction, bankLeitZahl), soapEndpointUrl);

            // Print the SOAP Response
            System.out.println("Response SOAP Message:");
            soapResponse.writeTo(System.out);
            System.out.println();
//            SOAPBody body = soapResponse.getSOAPBody();
//            System.out.println(body.getElementsByTagName("bezeichnnung").item(0).getTextContent());

            soapConnection.close();
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
        }
    }

    private static SOAPMessage createSOAPRequest(String soapAction, int bankLeitZahl) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage, bankLeitZahl);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();

        /* Print the request message, just for debugging purposes */
        System.out.println("Request SOAP Message:");
        soapMessage.writeTo(System.out);

        System.out.println("\n");

        return soapMessage;
    }

}
