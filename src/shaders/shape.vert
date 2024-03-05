#version 410 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec3 aColor;
layout (location = 3) in mat4x4 aMat;

uniform mat4x4 cameraMatrix;

out vec3 fragPos;
out vec3 normal;
out vec3 color;
out float smoothness;
void main()
{
    fragPos = (aMat * vec4(aPos, 1)).xyz;
    normal = aNormal;
    color = aColor;
    smoothness = 0.5f;

    gl_Position = cameraMatrix * aMat * vec4(aPos,1);
}