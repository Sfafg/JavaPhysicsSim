#version 420 core

in vec3 fragPos;
in vec3 normal;
in vec3 albedo;
in float smoothness;

layout (location = 0) out vec4 gAlbedoSmoothenss;
layout (location = 1) out vec3 gPosition;
layout (location = 2) out vec3 gNormal;

void main()
{
    gAlbedoSmoothenss = vec4(albedo,smoothness);
    gPosition = fragPos;
    gNormal = normal;
}