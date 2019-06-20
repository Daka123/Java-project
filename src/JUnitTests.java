import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
/*import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;*/

import java.io.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.*;

public class JUnitTests {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    public Client client;
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void UncommonFilesTest() throws IOException {
        List<String> all = new ArrayList<>();
        all.add("ala");
        all.add("ma");
        all.add("kota");
        List<String> without = new ArrayList<>();
        without.add("ala");
        List<String> expected = new ArrayList<>();
        expected.add("ma");
        expected.add("kota");
        Multi_Client mc = new Multi_Client(new ServerSocket(1),new Socket("localhost",1));
        assertEquals(mc.uncommon_files(all,without),expected);
    }

    @Test
    public void GetFileNameTest(){
        Discs disc = new Discs();
        assertEquals(disc.get_file_name("ala\\ma\\kota\\test.txt"),"test.txt");
    }

    @Test
    public void GetFileListTest(){
        MyMap test = ControlClient.get_file_list("E:\\Sem4\\PO2 aka Java\\Project\\src\\Classes\\tests", "test");
        List<String> list= new ArrayList<>();
        list.add("test1.txt");
        list.add("test2.txt");
        MyMap expected = new MyMap();
        expected.put("test",list);
        assertEquals(test,expected);
    }

    @Test
    public void MultiClientNullSocketTest(){
        try{
            new Multi_Client(new ServerSocket(666), null);
            fail("Expected exception due to null socket");
        } catch (Exception e) {}
    }

    @Test
    public void FindFileNullFileTest(){
        try{
            Share s = new Share("test","test","test");
            s.find_file(null,"test","test");
            fail("Expected exception due to null file");
        } catch (Exception e) {}
    }
    @Test
    public void FindFileNullFromClientTest(){
        try{
            Share s = new Share("test","test","test");
            s.find_file("test",null,"test");
            fail("Expected exception due to null from client");
        } catch (Exception e) {}
    }

    @Test
    public void FindFileNullToClientTest(){
        try{
            Share s = new Share("test","test","test");
            s.find_file("test","test",null);
            fail("Expected exception due to null to client");
        } catch (Exception e) {}
    }

    @Test
    public void ControlClientNullSocketTest(){
        try{
            new ControlClient(null,"test","test");
            fail("Expected exception due to null socket");
        } catch (Exception e) {}
    }
    @Test
    public void ControlClientNullFolderPathTest(){
        try{
            new ControlClient(new Socket(InetAddress.getLocalHost(),2137),null,"test");
            fail("Expeced exception due to null folder path");
        } catch (Exception e) {}
    }

    @Test
    public void ControlClientNullClientNameTest(){
        try{
            new ControlClient(new Socket(InetAddress.getLocalHost(),2137),"test",null);
            fail("Expected exception due to null client");
        } catch (Exception e) {}
    }

    @Test
    public void SendFilesMapNullStreamTest(){
        try{
            ControlClient.send_files_map(new MyMap(), null);
            fail("Expected exception due to null stream");
        } catch (Exception e) {}
    }

    @Test
    public void GetServerFilesListTest(){
        try{
            ControlClient c = new ControlClient(new Socket(InetAddress.getLocalHost(),2137),"test","test");
            c.get_server_files_list();
            fail("Expected exception due to server not being run");
        } catch (Exception e) {}
    }

    @Test
    public void GetFilesTest(){
        try{
            ControlClient c = new ControlClient(new Socket(InetAddress.getLocalHost(),2137),"test","test");
            c.get_files(null);
            fail("Expected exception due to null list");
        } catch (Exception e) {}
    }

    @Test
    public void ClientNullAddressTest(){
        try{
            new Client(null, 2137, "test","test");
            fail("Expected exception due to null address");
        } catch (Exception e) {}
    }

    @Test
    public void ClientNullClientTest(){
        try{
            new Client(InetAddress.getLocalHost(), 2137, null,"test");
            fail("Expected exception due to null client");
        } catch (Exception e) {}
    }

    @Test
    public void ClientNullFolderPathTest(){
        try{
            new Client(InetAddress.getLocalHost(), 2137, "test",null);
            fail("Expected exception due to null folder path");
        } catch (Exception e) {}
    }

    @Test
    public void GetClientFilesMapTest(){
        try{
            Multi_Client mc = new Multi_Client(new ServerSocket(), new Socket());
            mc.get_client_files_map();
            fail("Expected exception due to client not running");
        } catch (Exception e) {}
    }

    @Test
    public void SendFilesListTest(){
        try{
            Multi_Client.send_files_list(new ArrayList<>(),null);
            fail("Expected exception due to null stream");
        } catch (Exception e) {}
    }

    @Test
    public void GetClientListTest(){
        try{
            Multi_Client.get_clients_list(null);
            fail("Expected exception due to null client");
        } catch (Exception e) {}
    }

    @Test
    public void SynchroClientNullServerFilesTest(){
        try{
            Multi_Client mc = new Multi_Client(new ServerSocket(420), new Socket(InetAddress.getLocalHost(),420));
            mc.synchro_client(null, new MyMap());
            fail("Expected exception due to null server file list");
        } catch (Exception e) {}
    }

    @Test
    public void SynchroClientNullClientFilesTest(){
        try{
            Multi_Client mc = new Multi_Client(new ServerSocket(420), new Socket(InetAddress.getLocalHost(),420));
            mc.synchro_client(new MyMap[2], null);
            fail("Expected exception due to null client file list");
        } catch (Exception e) {}
    }

    @Test
    public void SynchroServerNullServerFilesTest(){
        try{
            Multi_Client mc = new Multi_Client(new ServerSocket(420), new Socket(InetAddress.getLocalHost(),420));
            mc.synchro_server(null, new MyMap(), "test");
            fail("Expected exception due to null server file list");
        } catch (Exception e) {}
    }

    @Test
    public void SynchroServerNullClientFilesTest(){
        try{
            Multi_Client mc = new Multi_Client(new ServerSocket(420), new Socket(InetAddress.getLocalHost(),420));
            mc.synchro_server(new MyMap[2], null, "test");
            fail("Expected exception due to null client file list");
        } catch (Exception e) {}
    }

    @Test
    public void SynchroServerNullClientPathTest(){
        try{
            Multi_Client mc = new Multi_Client(new ServerSocket(420), new Socket(InetAddress.getLocalHost(),420));
            mc.synchro_server(new MyMap[2], new MyMap(), null);
            fail("Expected exception due to null client path");
        } catch (Exception e) {}
    }

    @Test
    public void SynchroNullStreamTest(){
        try{
            Multi_Client mc = new Multi_Client(new ServerSocket(420), new Socket(InetAddress.getLocalHost(),420));
            mc.synchro(null, "test");
            fail("Expected exception due to null stream");
        } catch (Exception e) {}
    }

    @Test
    public void SynchroNullClientPathTest(){
        try{
            Multi_Client mc = new Multi_Client(new ServerSocket(420), new Socket(InetAddress.getLocalHost(),420));
            mc.synchro(System.in, null);
            fail("Expected exception due to null client path");
        } catch (Exception e) {}
    }

    @Test
    public void UpdateListNullFileTest(){
        try{
            Discs d = new Discs("test","test","test",new PrintWriter(System.out));
            d.update_list(null,"test","test");
            fail("Expected exception due to null file");
        } catch (Exception e) {}
    }

    @Test
    public void UpdateListNullClietTest(){
        try{
            Discs d = new Discs("test","test","test",new PrintWriter(System.out));
            d.update_list("test",null,"test");
            fail("Expected exception due to null client");
        } catch (Exception e) {}
    }

    @Test
    public void UpdateListNullDiscTest(){
        try{
            Discs d = new Discs("test","test","test",new PrintWriter(System.out));
            d.update_list("test","test",null);
            fail("Expected exception due to null disc");
        } catch (Exception e) {}
    }
}
