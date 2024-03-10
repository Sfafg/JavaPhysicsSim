#version 410 core

vec2 verts[] = vec2[6](
    vec2( 1,-1),
    vec2(-1,-1),
    vec2( 1, 1),
    vec2(-1,-1),
    vec2(-1, 1),
    vec2( 1, 1)
);

out vec2 uv;

void main()
{
    uv = verts[gl_VertexID] * 0.5 + 0.5;
    gl_Position = vec4(verts[gl_VertexID], 0, 1);
}