package org.apache.manifoldcf.csws;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.opentext.livelink.service.docman.Node;
import com.opentext.livelink.service.searchservices.SGraph;
import org.apache.commons.io.FileUtils;
import org.apache.manifoldcf.core.interfaces.ManifoldCFException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

@Ignore("wiremock tests are not runnable in ant build because mcf jetty version does not match")
public class InvalidCharInterceptorTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(0);

    private CswsSession sut;

    @Before
    public void setup() throws IOException, ManifoldCFException {
        stubFor(get(urlEqualTo("/authentication?wsdl"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/Authentication.wsdl")))));
        stubFor(get(urlEqualTo("/Authentication1.xsd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/Authentication1.xsd")))));
        stubFor(get(urlEqualTo("/Authentication2.xsd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/Authentication2.xsd")))));

        stubFor(get(urlEqualTo("/documentManagement?wsdl"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/DocumentManagement.wsdl")))));
        stubFor(get(urlEqualTo("/DocumentManagement1.xsd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/DocumentManagement1.xsd")))));
        stubFor(get(urlEqualTo("/DocumentManagement2.xsd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/DocumentManagement2.xsd")))));
        stubFor(get(urlEqualTo("/DocumentManagement3.xsd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/DocumentManagement3.xsd")))));

        stubFor(get(urlEqualTo("/memberService?wsdl"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/MemberService.wsdl")))));
        stubFor(get(urlEqualTo("/MemberService1.xsd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/MemberService1.xsd")))));
        stubFor(get(urlEqualTo("/MemberService2.xsd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/MemberService2.xsd")))));
        stubFor(get(urlEqualTo("/MemberService3.xsd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/MemberService3.xsd")))));

        stubFor(get(urlEqualTo("/contentService?wsdl"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/ContentService.wsdl")))));
        stubFor(get(urlEqualTo("/ContentService1.xsd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/ContentService1.xsd")))));
        stubFor(get(urlEqualTo("/ContentService2.xsd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/ContentService2.xsd")))));
        stubFor(get(urlEqualTo("/ContentService3.xsd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/ContentService3.xsd")))));

        stubFor(get(urlEqualTo("/searchService?wsdl"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/SearchService.wsdl")))));
        stubFor(get(urlEqualTo("/SearchService1.xsd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/SearchService1.xsd")))));
        stubFor(get(urlEqualTo("/SearchService2.xsd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(new File("wsdls/SearchService2.xsd")))));

        sut = new CswsSession(
                "user",
                "pw",
                null,
                0L,
                "http://localhost:" + wireMockRule.port() + "/authentication",
                "http://localhost:" + wireMockRule.port() + "/documentManagement",
                "http://localhost:" + wireMockRule.port() + "/contentService",
                "http://localhost:" + wireMockRule.port() + "/memberService",
                "http://localhost:" + wireMockRule.port() + "/searchService"
        );
    }

    @Test
    public void testInvalidCharInterceptorIsInstalledInDocumentManagementHandle() throws Exception {

        stubAuthentication();

        stubFor(post(urlEqualTo("/documentManagement"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                                "   <S:Header>\n" +
                                "      <OTAuthentication xmlns=\"urn:api.ecm.opentext.com\">\n" +
                                "         <AuthenticationToken>bla bla bla</AuthenticationToken>\n" +
                                "      </OTAuthentication>\n" +
                                "   </S:Header>\n" +
                                "   <S:Body>\n" +
                                "      <GetNodeResponse xmlns=\"urn:DocMan.service.livelink.opentext.com\" xmlns:ns2=\"urn:Core.service.livelink.opentext.com\">\n" +
                                "         <GetNodeResult>\n" +
                                "            <ID>13888124</ID>\n" +
                                "            <Name>Evil\u000BName</Name>\n" +
                                "         </GetNodeResult>\n" +
                                "      </GetNodeResponse>\n" +
                                "   </S:Body>\n" +
                                "</S:Envelope>")));



        Node node = sut.getNode(13888124);
        assertEquals("Evil Name", node.getName());
    }

    @Test
    public void testInvalidCharInterceptorIsInstalledInSearchServiceHandle() throws Exception {

        stubAuthentication();

        stubFor(post(urlEqualTo("/searchService"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("<?xml version='1.0' encoding='UTF-8'?>\n" +
                                "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                                "    <S:Header>\n" +
                                "        <OTAuthentication xmlns=\"urn:api.ecm.opentext.com\">\n" +
                                "            <AuthenticationToken>\n" +
                                "                token\n" +
                                "            </AuthenticationToken>\n" +
                                "        </OTAuthentication>\n" +
                                "    </S:Header>\n" +
                                "    <S:Body>\n" +
                                "        <SearchResponse xmlns=\"urn:SearchServices.service.livelink.opentext.com\">\n" +
                                "            <SearchResult>\n" +
                                "                <Results>\n" +
                                "                    <Item>\n" +
                                "                        <ID>DataId=27771122&amp;Version=0</ID>\n" +
                                "                        <N>\n" +
                                "                            <ID>n1</ID>\n" +
                                "                            <S>27771122</S>\n" +
                                "                            <S>Evil\u000BName</S>\n" +
                                "                            <S>Folder</S>\n" +
                                "                            <Type>t1</Type>\n" +
                                "                        </N>\n" +
                                "                    </Item>\n" +
                                "                    <ListDescription>\n" +
                                "                        <ActualCount>1</ActualCount>\n" +
                                "                        <IncludeCount>1</IncludeCount>\n" +
                                "                        <ListHead>1</ListHead>\n" +
                                "                    </ListDescription>\n" +
                                "                    <Type>\n" +
                                "                        <ID>t1</ID>\n" +
                                "                        <Strings>OTDataID</Strings>\n" +
                                "                        <Strings>OTName</Strings>\n" +
                                "                        <Strings>OTSubTypeName</Strings>\n" +
                                "                    </Type>\n" +
                                "                </Results>\n" +
                                "                <Type>\n" +
                                "                    <ID>MRD</ID>\n" +
                                "                    <Strings>OTDataID</Strings>\n" +
                                "                    <Strings>OTSubTypeName</Strings>\n" +
                                "                    <Strings>OTName</Strings>\n" +
                                "                </Type>\n" +
                                "            </SearchResult>\n" +
                                "        </SearchResponse>\n" +
                                "    </S:Body>\n" +
                                "</S:Envelope>\n")));



        List<? extends SGraph> result = sut.searchFor(
                13888124,
                new String[0],
                "",
                "", "",
                0,
                10);
        assertEquals("Evil Name", result.get(0).getN().get(0).getS().get(1));
    }

    private void stubAuthentication() {
        stubFor(post(urlEqualTo("/authentication"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                                "   <S:Body>\n" +
                                "      <AuthenticateUserResponse xmlns=\"urn:Core.service.livelink.opentext.com\">\n" +
                                "         <AuthenticateUserResult>ein_schoenes_access_token</AuthenticateUserResult>\n" +
                                "      </AuthenticateUserResponse>\n" +
                                "   </S:Body>\n" +
                                "</S:Envelope>")));
    }
}
