### Using mail SMTP:
1. Turn off 2-Step Verification: https://support.google.com/accounts/answer/1064203?hl=en
2. Allow Less Secure App(should be turned on): https://myaccount.google.com/lesssecureapps

### Run local:
1. Create DB: lsge, change config application.yml  
2. Run client:  
    >`cd web-client`  
    `yarn serve`

3. Run backend  
    >run: `mvn spring-boot:run`  
    debug: `mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=$PORT_NUMBER"`

4. Run echo server  
    >`cd echo-server`  
    `npm start` | `laravel-echo-server start`  
  
5. Run laravel API server  
    >`cd echo-server`  
    `php artisan serve`

6. Install redis or run docker with docker-compose.yml

7. URL  
    > UI: http://localhost:8080   
    Backend API: http://localhost:18081  
    Echo server: http://localhost:6001  
    Laravel API server: http://localhost:8000  
    Redis: http://localhost:6379