#version 410 core

in vec4 gl_FragCoord;
in vec3 normal;
uniform ivec2 Resolution;

void main()
{
    gl_FragColor = vec4(vec3(normal),1);
}