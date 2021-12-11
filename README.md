# CentriVaccinaliServer

Il progetto centri vaccinali è suddiviso in due parti:
<ol>
<li>Centri vaccinali client</li>
<li>Centri vaccinali server</li>
</ol>
Questo ReadMe riguarda il lato server.

Le principali librerie esterne utilizzate sono:

<ol>
<li>
postgresql
<ul>
<li>Utilizzata per interfacciarsi con il DataBase (JDBC)</li>
</ul>

<li>
password4j
<ul>
<li>Utilizzata per gestire criptaggio e decriptaggio delle password</li>
</ul>

<li>
javax.mail
<ul>
<li>Utilizzata per inviare le email di verifica</li>
</ul>

<li>
commons-io
<ul>
<li>Utilizzata per leggere i file</li>
</ul>
</ol>

Per poter installare e compilare correttamente il progetto, è necessario configurare una nuova applicazione in questo
modo:

![Immagine di esempio configurazione](https://cdn.discordapp.com/attachments/893484185036152869/919251266570432523/Server.PNG)