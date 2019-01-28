#version 140

in vec2 pass_TextureCoords;

out vec4 out_Color;

uniform sampler2D sampler;
uniform vec4 color;

void main() {
	out_Color = vec4(1.0,1.0,1.0,1.0);
}