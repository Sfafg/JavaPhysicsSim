#version 420 core

in vec2 uv;

layout(binding = 0) uniform sampler2D gAlbedoSmoothness;
layout(binding = 1) uniform sampler2D gPosition;
layout(binding = 2) uniform sampler2D gNormal;
layout(binding = 3) uniform sampler2D gDepth;
layout(binding = 4) uniform sampler2D ssaoTexture;
uniform vec3 lightPos;
uniform vec3 cameraPos;
uniform ivec2 resolution;
uniform float nearPlane;
uniform float farPlane;

out vec4 fragColor;

float Depth(vec2 uv, vec2 offset)
{
    float depth = texture(gDepth, uv + offset / resolution ).r; 
    return (2.0 * nearPlane) / (farPlane + nearPlane - depth * (farPlane - nearPlane));   
}

void main()
{    
    float depth = Depth(uv, vec2(0,0));
    vec3 e = vec3(-1,0,1) ;
    float s = depth * 4 - Depth(uv, e.xy)- Depth(uv, e.yz)- Depth(uv, e.zy)- Depth(uv, e.yx);
    if(abs(s) > mix(0.001,1.0,depth * depth))
    {
        fragColor = vec4(0,0,0,1);
    }
    else  
    {
        vec3 albedo = texture(gAlbedoSmoothness, uv).rgb;
        float smoothness = texture(gAlbedoSmoothness, uv).w;
        vec3 p = texture(gPosition, uv).xyz;
        vec3 normal = -(texture(gNormal, uv).xyz);
        normal.z = -normal.z;
        float ambientOclusion = texture(ssaoTexture,uv).r;

        vec3 toLight = normalize(lightPos - p);
        vec3 toCamera = normalize(cameraPos - p);

        float gooch = dot(normal,toLight) * 0.5 + 0.5;
        vec3 cold = mix(vec3(0,0,1), albedo, 0.4);
        vec3 warm = mix(vec3(1,1,0), albedo, 0.6);

        vec3 diffuse = gooch * warm + (1 - gooch) * cold;
        vec3 reflectionDir = reflect(-toLight, normal);
        float specular = max(0,dot(toCamera, reflectionDir));
        specular = pow(specular, smoothness * 100);
        if(ambientOclusion == 0) ambientOclusion = 1;
        fragColor = vec4((diffuse + specular * smoothness) * ambientOclusion ,1);
    }
}