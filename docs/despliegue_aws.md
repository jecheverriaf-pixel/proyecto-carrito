# Despliegue en AWS (capa gratuita) — CarritoGT


>
> Para crear estos recursos se necesita una cuenta y una tarjeta de AWS, así que eso lo tiene que hacer cada
> quien con la suya, pero en cada paso dejé el comando exacto que hay que ejecutar.

## 0. Antes de empezar: subir el proyecto a GitHub

Hace falta un repositorio remoto para poder clonarlo dentro de la instancia EC2 (es más fácil que copiar
archivos a mano). El historial de commits ya lo tenía en local, solo faltaba el remoto:

1. Crear un repositorio **privado o público** en https://github.com/new (por ejemplo `carrito-compras`). Ojo:
   no marcar "Initialize with README", porque el proyecto ya trae uno.
2. Conectarlo y subir el historial:
   ```powershell
   git remote add origin https://github.com/TU_USUARIO/carrito-compras.git
   git push -u origin master
   ```

`config/application.properties` (donde está la contraseña real) **no se sube**, porque ya está en `.gitignore`.
Igual conviene revisar con `git status` que no aparezca antes de hacer push.

## 1. Crear la cuenta de AWS Free Tier

1. Entrar a https://aws.amazon.com/free y crear una cuenta con un correo.
2. Piden una tarjeta de crédito/débito para verificar la identidad. **No cobran nada** mientras uno se
   mantenga dentro de los límites de la capa gratuita (750 horas/mes de EC2 `t2.micro`/`t3.micro` durante
   los primeros 12 meses).
3. Activar **MFA (autenticación multifactor)** en la cuenta raíz (root). Está en "IAM" → "Security credentials".
4. **Recomendado**: crear un usuario IAM administrador para el uso diario y no trabajar como root
   (IAM → Users → Create user → adjuntar la política `AdministratorAccess`). De ahí en adelante se inicia
   sesión con ese usuario.
5. **Configurar una alerta de facturación** (AWS Budgets → Create budget → "Zero spend budget") para que
   avise por correo si algo empieza a generar cargo. Esto sirve mucho para no llevarse una sorpresa.

## 2. Lanzar la instancia EC2

En la consola de AWS, buscar **EC2** → **Launch instance**:

| Campo | Valor |
|---|---|
| Name | `carritogt-server` |
| AMI (imagen) | **Amazon Linux 2023** (entra en el free tier, trae `dnf` y tiene buen soporte para Java) |
| Instance type | `t2.micro` o `t3.micro` (hay que ver cuál dice "Free tier eligible" en la región) |
| Key pair | Crear una nueva (`carritogt-key`), formato `.pem`, **descargarla y guardarla** porque no se puede volver a descargar |
| Network settings | Dejar la VPC/subred que viene por defecto |
| Security group | Crear uno nuevo, las reglas están abajo |
| Storage | 8-20 GB gp3 (el free tier incluye hasta 30 GB) |

**Reglas del Security Group** (es lo mismo que el reenvío de puertos que configuré en VirtualBox):

| Tipo | Puerto | Origen | Para qué |
|---|---|---|---|
| SSH | 22 | **Tu IP** (elegir "My IP", no `0.0.0.0/0`) | Conectarse por SSH de forma segura |
| Custom TCP | 8080 | `0.0.0.0/0` | Entrar a la app Spring Boot desde cualquier navegador |

Lanzar la instancia y esperar a que el estado diga "Running". Después copiar su **IPv4 pública**.

## 3. Conectarse por SSH desde la máquina virtual Linux

La conexión la hice desde la terminal de mi máquina virtual Linux (la misma VM de VirtualBox), no desde
Windows. La VM sale a internet por el adaptador NAT, así que AWS la ve con la misma IP pública que el equipo
anfitrión y la regla "My IP" del Security Group funciona igual.

Primero hay que tener el archivo `carritogt-key.pem` dentro de la VM (descargarlo desde el navegador de la VM
o copiarlo desde Windows). Después, en la terminal de la VM:

```bash
# mover la clave a ~/.ssh
mkdir -p ~/.ssh
mv ~/Descargas/carritogt-key.pem ~/.ssh/    # o ~/Downloads, según el idioma de la VM

# ssh rechaza la clave si otros usuarios la pueden leer, por eso se deja solo lectura para el dueño
chmod 400 ~/.ssh/carritogt-key.pem

ssh -i ~/.ssh/carritogt-key.pem ec2-user@TU_IP_PUBLICA
```

La primera vez pregunta si se confía en el host (`Are you sure you want to continue connecting`), se responde
`yes`.

## 4. Instalar dependencias dentro de la instancia

```bash
sudo dnf update -y

# Java 21 (Amazon Corretto)
sudo dnf install -y java-21-amazon-corretto

# Git
sudo dnf install -y git

# MariaDB (compatible con MySQL, disponible en los repos de Amazon Linux 2023)
sudo dnf install -y mariadb105-server
sudo systemctl enable --now mariadb
sudo systemctl status mariadb
```

## 5. Configurar la base de datos

```bash
sudo mysql_secure_installation
sudo mysql -u root -p
```

Dentro del cliente:
```sql
CREATE DATABASE cc CHARACTER SET utf8mb4;
EXIT;
```

## 6. Clonar y configurar el proyecto

```bash
git clone https://github.com/TU_USUARIO/carrito-compras.git
cd carrito-compras

mkdir config
nano config/application.properties
```

Contenido de `config/application.properties` (hay que poner la contraseña que se definió en `mysql_secure_installation`):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cc?useUnicode=true&characterEncoding=utf8
spring.datasource.username=root
spring.datasource.password=tu-contrasena-de-mysql-en-aws
```

## 7. Compilar y probar en primer plano

```bash
chmod +x mvnw
./mvnw clean package -DskipTests
java -jar target/carrito-compras-0.0.1-SNAPSHOT.jar
```

Si todo sale bien aparece `Started CarritoComprasApplication` y `Tomcat started on port 8080`. Para probar,
abrir en el navegador (sirve el de la VM o el de Windows, porque la IP es pública):
```
http://TU_IP_PUBLICA:8080
```

Antes de seguir, detener la prueba con `Ctrl+C`.

## 8. Dejarlo corriendo como servicio (systemd)

Esto es para que la app siga corriendo aunque se cierre la sesión SSH, y para que arranque sola si la
instancia se reinicia:

```bash
sudo tee /etc/systemd/system/carritogt.service > /dev/null <<'EOF'
[Unit]
Description=CarritoGT Spring Boot App
After=network.target mariadb.service

[Service]
User=ec2-user
WorkingDirectory=/home/ec2-user/carrito-compras
ExecStart=/usr/bin/java -jar /home/ec2-user/carrito-compras/target/carrito-compras-0.0.1-SNAPSHOT.jar
Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable --now carritogt
sudo systemctl status carritogt
journalctl -u carritogt -f      # ver logs en vivo, Ctrl+C para salir
```

Volver a entrar a `http://TU_IP_PUBLICA:8080`. Ahora ya no depende de tener la terminal SSH abierta.

## 9. Actualizar el despliegue cuando cambia el código

```bash
cd ~/carrito-compras
git pull
./mvnw clean package -DskipTests
sudo systemctl restart carritogt
```

## 10. Cuidar el free tier (para que no cobren)

- **Detener** la instancia (Stop, no Terminate) cuando no se esté usando para la defensa/demo, con
  `Instance state → Stop`. Una instancia detenida no gasta horas de cómputo, aunque el disco EBS sí sigue
  contando (también entra en el free tier hasta 30 GB).
- Ojo: **la IP pública cambia** cada vez que se detiene y se vuelve a arrancar la instancia, a menos que se
  asigne una Elastic IP (que es gratis solo mientras esté asociada a una instancia corriendo). Si se necesita
  una IP fija para el video/defensa, se asigna una Elastic IP y se libera cuando ya no se use.
- Revisar de vez en cuando el dashboard de **AWS Billing → Free Tier usage**.
- Cuando termine el semestre y ya no se necesite: hacer **Terminate** de la instancia. Esto sí borra todo,
  incluida la base de datos, así que antes hay que asegurarse de tener el script SQL y el código a salvo en GitHub.


