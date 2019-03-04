#include <ESP8266WiFi.h>
#include <DHTesp.h>
#include <MySQL_Connection.h>
#include <MySQL_Cursor.h>
//#include <WiFiClient.h>

//deifne pinos utilizados pelo sistema
#define dhtPin 4
#define infoRele 1
#define sensorUmidade A0

//variaveis que definem intervalo de açoes do sistema
static unsigned long tempoestatisticas = millis();
static unsigned long tempohistoricodiario = millis();
static unsigned long tempoalteracao = millis();
static unsigned long tempoTerminaIrrigacao = millis();
static unsigned long tempoIniciaAlt;
long tempoMilli = 0;

//variaveis
bool ativacao = false;
bool ativacao2 = true;
bool ativacao3 = false;
bool i = false;

char tempo[9];

int id_estufa = 1;
char* stat = "A";
int id_alt;

DHTesp dht;
int umidade;
double temperatura;
char status_alt[2];

//conexao com internet
const char* ssid     = "TesteMecatronica";
const char* password = "teste1234";
byte mac[6];

WiFiServer server(80);
IPAddress ip(192, 168, 1, 50);
IPAddress gateway(192, 168, 1, 1);
IPAddress subnet(255, 255, 255, 0);

WiFiClient client;
MySQL_Connection conn((Client*)&client);

//string de requisicoes no banco
char SELECT_CANCELAMENTO[] = "SELECT STATUS_SYS FROM alteracoes WHERE Id_Estufa = %i AND Id_alteracao = %i";
char UPDATE_STATUS_SQL[] = "UPDATE alteracoes SET STATUS_SYS = '%s' WHERE Id_Estufa = %i AND Id_alteracao = %i";
char SELECT_SQL[] = "SELECT Tempo_funcionamento,Id_alteracao FROM alteracoes WHERE STATUS_SYS = '%s' AND Id_Estufa = %i";
char INSERT_SQL[] = "INSERT INTO historico_diario(Horario_historico,Data_historico,Temperatura_historico, Umidade_historico, STATUS_SYS, Id_estufa) VALUES ( NOW(), NOW(), %.1f, %i, '%s', %i)";
char UPDATE_SQL[] = "UPDATE estufas SET Temperatura_Estufa = %.1f, Umidade_Estufa = %i WHERE Id_estufa = %i";
char BANCODEDADOS[] = "USE bd_estufa";
char query[200];

IPAddress server_addr(192, 168, 1, 101);
char userBanco[] = "arduino2";
char senhaBanco[] = "root";

void setup() {
  delay(500);

  Serial.begin(115200);

  while (!Serial);

  WiFi.begin(ssid, password);
  WiFi.config(ip, gateway, subnet);

  while ( WiFi.status() != WL_CONNECTED ) {
    delay(100);
    Serial.print(".");
  }

  pinMode(infoRele,OUTPUT);
  pinMode(sensorUmidade, INPUT);

  Serial.println('\n');
  Serial.print("Conectado a rede: ");
  Serial.print(ssid);

  Serial.println('\n');
  Serial.print("Endereço de IP: ");
  Serial.print(ip);

  Serial.println('\n');
  WiFi.macAddress(mac);
  Serial.print("MAC: ");
  Serial.print(mac[5], HEX);
  Serial.print(":");
  Serial.print(mac[4], HEX);
  Serial.print(":");
  Serial.print(mac[3], HEX);
  Serial.print(":");
  Serial.print(mac[2], HEX);
  Serial.print(":");
  Serial.print(mac[1], HEX);
  Serial.print(":");
  Serial.println(mac[0], HEX);
  Serial.println("");

  Serial.println('\n');
  Serial.print("Conectando com banco de dados");
  Serial.println("");

  while (conn.connect(server_addr, 3306, userBanco, senhaBanco) != true) {
    delay(200);
    Serial.print ( "." );
  }

  MySQL_Cursor *cur_mem = new MySQL_Cursor(&conn);
  cur_mem->execute(BANCODEDADOS);
  delete cur_mem;

  Serial.println("");
  Serial.println("Conectado ao servidor SQL!");
  Serial.println("");
}

void loop() {
  //pega temperatura e umidade e insere no banco tabela estufa
  if (millis() >= tempoestatisticas + 10000) {
    //sensor temperatura
    dht.setup(dhtPin, DHTesp::DHT11);
    TempAndHumidity lastValues = dht.getTempAndHumidity();

    //sensor umidade do solo
    for (int i = 0; i < 3; i++) {
      umidade = analogRead(sensorUmidade);
    }

    umidade = 100 * ((978 - (float)umidade) / 978);
    temperatura = lastValues.temperature;

    sprintf(query, UPDATE_SQL, temperatura, umidade, id_estufa);
    Serial.println("");
    Serial.println("Atualizando temperatura e umidade.");
    Serial.println("");

    MySQL_Cursor *cur_mem = new MySQL_Cursor(&conn);

    cur_mem->execute(query);
    delete cur_mem;
    tempoestatisticas = millis();
  }

  //pega informaçoes tais como data,hora,umidade,temperatura e insere no banco tabela historico_diario
  if (millis() >= tempohistoricodiario + 600000) {

    dht.setup(dhtPin, DHTesp::DHT11);
    TempAndHumidity lastValues = dht.getTempAndHumidity();
    temperatura = lastValues.temperature;

    for (int i = 0; i < 3; i++) {
      umidade = analogRead(sensorUmidade);
      Serial.println(umidade);
    }

    umidade = 100 * ((1023 - (float)umidade) / 1023);

    Serial.println("Gravando informação tabela historico.");
    sprintf(query, INSERT_SQL, temperatura, umidade, stat, id_estufa);

    MySQL_Cursor *cur_mem1 = new MySQL_Cursor(&conn);

    cur_mem1->execute(query);

    delete cur_mem1;

    tempohistoricodiario = millis();
  }

  //--------------pega alteracoes feitas para iniciar irrigacao--------------
  if (millis() >= tempoalteracao + 2000) {

    long head_count = 0;
    row_values *row = NULL;
    char id_alteracao[11];

    if (!ativacao) {
      sprintf(query, SELECT_SQL, stat, id_estufa);

      MySQL_Cursor *cur_mem2 = new MySQL_Cursor(&conn);

      cur_mem2->execute(query);

      column_names *columns = cur_mem2->get_columns();

      for (int f = 0; f < columns->num_fields; f++) {
        Serial.print(columns->fields[f]->name);
        if (f < columns->num_fields - 1) {
          Serial.print(", ");
        }
      }
      Serial.print(": ");
      do {
        row = cur_mem2->get_next_row();
        if (row != NULL) {
          sprintf(tempo, "%s", row->values[0]);
          Serial.print(tempo);
          Serial.print(", ");
          sprintf(id_alteracao, "%s", row->values[1]);
          Serial.print(id_alteracao);
        }
      } while (row != NULL);
      Serial.println("");

      id_alt = (id_alteracao[0] - 48) * 10;
      id_alt = ((id_alteracao[1] - 48) + id_alt) * 10;
      id_alt = ((id_alteracao[2] - 48) + id_alt) * 10;
      id_alt = ((id_alteracao[3] - 48) + id_alt);

      delete cur_mem2;
    }

    tempoMilli = (tempo[0] - 48) * 36000000;
    tempoMilli = tempoMilli + (tempo[1] - 48) * 3600000;
    tempoMilli = tempoMilli + (tempo[3] - 48) * 600000;
    tempoMilli = tempoMilli + (tempo[4] - 48) * 60000;
    tempoMilli = tempoMilli + (tempo[6] - 48) * 10000;
    tempoMilli = tempoMilli + (tempo[7] - 48) * 1000;

    tempoMilli = tempoMilli - 850;

    if (tempoMilli > 0) {
      Serial.println("Passei no b");
      static unsigned long tempoIniciaAlt = millis();
      tempoalteracao = millis();
      iniciaAcao();
      ativacao3 = true;
      ativacao2 = true;
    }

    tempoalteracao = millis();
  }
  //-------------------------------------------------------------------------------------

  //---------------------cancelamento irrigação-------------------------
  if (ativacao3) {
    if (millis() >= tempoTerminaIrrigacao + 2500) {
      //char* status_off = "I";
      row_values *row = NULL;

      Serial.print("");
      Serial.print("Id alteracao: ");
      Serial.println(id_alt);
      Serial.print("");
      sprintf(query, SELECT_CANCELAMENTO, id_estufa, id_alt);
      MySQL_Cursor *cur_mem4 = new MySQL_Cursor(&conn);

      cur_mem4->execute(query);

      column_names *columns = cur_mem4->get_columns();
      do {
        row = cur_mem4->get_next_row();
        if (row != NULL && ativacao2) {
          sprintf(status_alt, "%s", row->values[0]);

        }
      } while (row != NULL);

      if (!strcmp(status_alt, "I")) {
        paraAcao();
        Serial.printf("Irrigacao cancelada");
        sprintf(status_alt, "%s", "T");
        ativacao2 = false;
      }

      delete cur_mem4;
      tempoTerminaIrrigacao = millis();
    }
  }
  //-----------------------------------------------------------
}

//iniciaAcao() inicia irrigacao
void iniciaAcao() {
  ativacao = true;
  digitalWrite(infoRele, HIGH);

  Serial.print("");
  Serial.print("Tempo total: ");
  Serial.println(tempoIniciaAlt + tempoMilli);

  Serial.print("Tempo atual: ");
  Serial.println(millis());
  Serial.println("");

  if (i == false) {
    tempoIniciaAlt = millis();
    i = true;
  }

  if (millis() >= tempoIniciaAlt + tempoMilli) {
    paraAcao();
    Serial.println("Acabou o tempo!!!");
  }
}

void paraAcao() {
  digitalWrite(infoRele, LOW);
  char* sta = "I";
  sprintf(query, UPDATE_STATUS_SQL, sta, id_estufa, id_alt);
  MySQL_Cursor *cur_mem3 = new MySQL_Cursor(&conn);

  cur_mem3->execute(query);

  delete cur_mem3;
  tempo[0] = 48;
  tempo[1] = 48;
  tempo[3] = 48;
  tempo[4] = 48;
  tempo[6] = 48;
  tempo[7] = 48;

  tempoMilli = 0;
  ativacao = false;
  ativacao3 = false;
  i = false;
}
