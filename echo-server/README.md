####Build project Echo Server
1. Copy .env.example to .env  
Edit attribute env:
    - `Mysql database`
    - `Redis database`
    - `LSGE_API_SERVICE`: `{domain service Java}/api`
    - `BROADCAST_DRIVER`: `redis`

2. Composer install
3. Generate key
    - `php artisan key:generate`
4. Generate jwt secret
    - `php artisan jwt:secret`
5. install laravel echo server
    - `npm install -g laravel-echo-server`
    - Update `laravel-echo-server.json` file:
        + `authHost`
        + `authEndpoint`
        + `database`: `redis`
        + `databaseConfig.redis`:
            ```json
            {
                "host": "127.0.0.1",
                "port": "6379",
                "password": null,
                "db": 2
            }
            ```
        + `port`: default `6001`
    - run `laravel-echo-server`: 
        + `yarn start` or
        + `npm run start` or
        + `laravel-echo-server start`
6. Run project:
    - Manual run:
        + `php artisan serve`
    - Config auto run with apache:
        + install apache2
        + Enable mod_rewrite for Apache:
            - `sudo a2enmod rewrite`
            - `sudo service apache2 restart`
            - `sudo apachectl -t -D DUMP_MODULES | grep rewrite`
        + add config file into `/etc/apache2/sites-available` folder:
            - Example: file `echo-server.conf`
                ```
                NameVirtualHost *:8080
                Listen 8080
                 
                <VirtualHost *:8080>
                    ServerAdmin admin@example.com
                    ServerName echo-server.dev
                    ServerAlias www.echo-server.dev
                    DocumentRoot /path/to/project/echo-server/public
                     
                    <Directory /path/to/project/echo-server/public/>
                            Options Indexes FollowSymLinks MultiViews
                            AllowOverride All
                            Order allow,deny
                            allow from all
                            Require all granted
                    </Directory>
                     
                    LogLevel debug
                    ErrorLog ${APACHE_LOG_DIR}/error.log
                    CustomLog ${APACHE_LOG_DIR}/access.log combined
                </VirtualHost>
                ```
        + add ip config into hosts:
            - `127.0.0.1   echo-server.dev`
        + Enable new virtual host and disable the default virtual host:
            - `sudo a2ensite echo-server.conf`
            - `sudo a2dissite 000-default.conf`
            - `sudo services apache2 restart`
    - Config auto run with nginx:
        + install nginx
        + add config file into `/etc/nginx/conf.d` folder
            - Example: file `echo-server.conf`
                ```
                server {
                    listen 80;
                    listen [::]:80;
                    listen 443 ssl;
                    listen [::]:443 ssl;
                    ssl_certificate     /etc/ssl/certs/ssl-cert-snakeoil.pem;
                    ssl_certificate_key /etc/ssl/private/ssl-cert-snakeoil.key;
                    root /path/to/project/echo-server/public;
                    index index.php index.html index.htm index.nginx-debian.html;
                    server_name echo-server.dev;
                    location / {
                        try_files $uri $uri/ /index.php?$query_string;
                    }
                    location ~ \.php$ {
                        include         snippets/fastcgi-php.conf;
                        fastcgi_pass    unix:/var/run/php/php7.2-fpm.sock;
                        fastcgi_param   SCRIPT_FILENAME $document_root$fastcgi_script_name;
                    }
                }
                ```
            + add ip config into hosts:
                - `127.0.0.1   echo-server.dev`
            + Restart nginx to enable new virtual host:
                - `sudo services nginx restart`
