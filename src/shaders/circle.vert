#version 410 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;

uniform mat4x4 cameraMatrix;  
uniform mat4x4 modelMatrix;

out vec3 normal;
void main()
{
    normal = aNormal;
    gl_Position = cameraMatrix * modelMatrix * vec4(aPos,1);
}