#version 330

in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D u_texture;

void main()
{
    fragColor = texture(u_texture, outTexCoord);
}