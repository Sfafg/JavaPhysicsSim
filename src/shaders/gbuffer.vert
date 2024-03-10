#version 410 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec3 aAlbedo;
layout (location = 3) in mat4x4 aMat;

uniform mat4x4 viewMatrix;
uniform mat4x4 projectionMatrix;

out vec3 fragPos;
out vec3 normal;
out vec3 albedo;
out float smoothness;

void main()
{
    fragPos = (aMat * vec4(aPos, 1.0)).xyz;
    normal = aNormal;
    albedo = aAlbedo;
    smoothness = 0.5f;

    gl_Position = projectionMatrix * viewMatrix * vec4(fragPos,1);
}