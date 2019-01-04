#version 330

in vec2 pass_TextureCoords;
in vec3 toLightVector[@maxLights];

out vec4 out_Color;

uniform sampler2D sampler;
uniform vec3 lightColor[@maxLights];
uniform float lightBrightness[@maxLights];

void main() {	
	out_Color = texture(sampler, pass_TextureCoords);
}