#version 410 core

in vec2 uv;

uniform sampler2D colorTex;
uniform sampler2D depthTex;


float Depth(vec2 uv) {
    float d = texture(depthTex, uv).x * 2 - 1 ;
    return 1 / (2.0 * 0.01 * 100) / (100 + 0.01 - d * (100 - 0.01));
}

float kernel[9] = float[9](
    -1, -1, -1,
    -1,  8, -1,
    -1, -1, -1
);

void main()
{
    vec3 e = vec3(-0.001, 0, 0.001);

    float sampled[9] = float[9](
        Depth(uv + e.xz),Depth(uv + e.yz),Depth(uv + e.zz),
        Depth(uv + e.xy),Depth(uv + e.yy),Depth(uv + e.zy),
        Depth(uv + e.xx),Depth(uv + e.yx),Depth(uv + e.zx)
    );
    float sum = 0;
    for(int i = 0; i < 9; i++)
    {
        sum += sampled[i] * kernel[i];
    }

    
    if(sum > 0.5)
    {
        gl_FragColor = vec4(vec3(0),1);
    }
    else
    {
        gl_FragColor = texture(colorTex,uv);
    }
}