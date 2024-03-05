#version 410 core

in vec3 fragPos;
in vec3 normal;
in vec3 color;
in float smoothness;

uniform vec3 lightPos;
uniform vec3 cameraPos;

void main()
{
    vec3 p = fragPos;
    vec3 toLight = normalize(lightPos - p);
    vec3 toCamera = normalize(cameraPos - p);

    float gooch = dot(normal,toLight) * 0.5 + 0.5;
    vec3 cold = vec3(0,0,1) + color * 0.4;
    vec3 warm = vec3(1,1,0) + color * 0.6;

    vec3 diffuse = gooch * warm + (1 - gooch) * cold;
    vec3 reflectionDir = reflect(-toLight, normal);
    float specular = max(0,dot(toCamera, reflectionDir));
    specular = pow(specular, smoothness * 100);

    gl_FragColor = vec4(diffuse + vec3(specular),1);
}