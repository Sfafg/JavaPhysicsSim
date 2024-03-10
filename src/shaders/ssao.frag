#version 420 core
in vec2 uv;

layout(binding = 0) uniform sampler2D gPosition;
layout(binding = 1) uniform sampler2D gNormal;
layout(binding = 2) uniform sampler2D ssaoNoise;
uniform mat4 view;
uniform mat4 projection;
uniform vec3 ssaoKernel[8];
uniform ivec2 resolution;


out float FragColor;

int kernelSize = 8;
float radius = 0.5;
float bias = 0.02;
void main()
{
    vec2 noiseScale = resolution / 4.0; 
    vec3 fragPos = (view * vec4(texture(gPosition, uv).xyz,1)).xyz;
    mat3 normalMatrix = transpose(inverse(mat3(view)));
    vec3 normal = normalMatrix * texture(gNormal, uv).rgb;
    vec3 randomVec = normalize(texture(ssaoNoise, uv * noiseScale).xyz);
    vec3 tangent = normalize(randomVec - normal * dot(randomVec, normal));
    vec3 bitangent = cross(normal, tangent);
    mat3 TBN = mat3(tangent, bitangent, normal);

    float occlusion = 0.0;
    for(int i = 0; i < kernelSize; ++i)
    {
        vec3 samplePos = TBN * ssaoKernel[i];
        samplePos = fragPos + samplePos * radius; 
        
        vec4 offset = vec4(samplePos, 1.0);
        offset = projection * offset;
        offset.xyz /= offset.w;
        offset.xyz = offset.xyz * 0.5 + 0.5;
    
        float sampleDepth = ((view * vec4(texture(gPosition, offset.xy).xyz,1)).z);
        float rangeCheck = smoothstep(0.0, 1.0, radius / abs((fragPos.z) - sampleDepth));
        occlusion += (sampleDepth >= (samplePos.z) + bias ? 1.0 : 0.0) * rangeCheck;           
    }
    occlusion = 1.0 - (occlusion / kernelSize);
    if(occlusion == 0) occlusion = 1;
    FragColor = occlusion;
}