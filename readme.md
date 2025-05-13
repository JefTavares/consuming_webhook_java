# Subir o ambiente

```sh
terraform init
```

```sh
terraform apply
```

```sh
terraform destroy
```

# Configuração do ambiente

# Instale pacotes necessários

sudo apt update
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common

## 2.Adicione a chave GPG do Docker

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

## 3. Adicione o repositório

sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"

## 4.Instale o Docker

sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io

## 5. Adicione seu usuário ao grupo docker (que agora existe):

sudo usermod -aG docker $USER

## 6. Aplique as alterações sem precisar reiniciar:

newgrp docker

## 7. Teste se o Docker está funcionando:

docker ps

## Instalação da AWS CLi

https://www.howtoforge.com/installing-aws-cli-on-ubuntu-24-04/

Para adicionar um certificado SSL na máquina EC2 que hospedará sua aplicação Spring Boot, você pode seguir os passos
abaixo:

1. **Obtenha um certificado SSL**:
    - Use um certificado gratuito como o [Let's Encrypt](https://letsencrypt.org/) ou adquira um certificado de uma
      autoridade certificadora (CA).
    - Para Let's Encrypt, você pode usar ferramentas como o [Certbot](https://certbot.eff.org/).

2. **Configure o certificado na aplicação Spring Boot**:
    - Converta o certificado SSL para o formato `.p12` (PKCS12), que é suportado pelo Spring Boot:
      ```bash
      openssl pkcs12 -export -in certificado.crt -inkey chave_privada.key -certfile cadeia_certificados.crt -out keystore.p12
      ```
    - Adicione o arquivo `keystore.p12` ao servidor EC2.

3. **Atualize o `application.properties` ou `application.yml`**:
    - Configure o Spring Boot para usar o certificado:
      ```properties
      server.ssl.key-store=classpath:keystore.p12
      server.ssl.key-store-password=sua_senha
      server.ssl.key-store-type=PKCS12
      server.port=443
      ```

4. **Abra a porta 443 no Security Group**:
    - No arquivo `infra/main.tf`, adicione a regra para a porta 443:
      ```terraform
      ingress {
        from_port = 443
        to_port   = 443
        protocol  = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
      }
      ```

5. **Reinicie a aplicação Spring Boot**:
    - Certifique-se de que a aplicação está configurada para rodar na porta 443 e com o SSL habilitado.

6. **Teste o acesso HTTPS**:
    - Acesse a aplicação via `https://<seu-dominio-ou-ip>` para verificar se o certificado está funcionando
      corretamente.